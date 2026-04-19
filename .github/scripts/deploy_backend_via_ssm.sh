#!/usr/bin/env bash
set -euo pipefail

APP_IMAGE="${APP_IMAGE:-}"
CONTAINER_NAME="${CONTAINER_NAME:-spring-api-system}"
HOST_PORT="${HOST_PORT:-8080}"
CONTAINER_PORT="${CONTAINER_PORT:-8080}"
HEALTHCHECK_URL="${HEALTHCHECK_URL:-http://localhost:${HOST_PORT}/}"

REQUIRED_RUNTIME_VARS=(
  SPRING_PROFILES_ACTIVE
  SPRING_DATASOURCE_URL
  SPRING_DATASOURCE_USERNAME
  SPRING_DATASOURCE_PASSWORD
  MAIL_HOST
  MAIL_PORT
  MAIL_USERNAME
  MAIL_PASSWORD
  PAG_API_URL
  INFOBIP_BASE_URL
  INFOBIP_API_KEY
  INFOBIP_WHATSAPP_SENDER
  INFOBIP_TEMPLATE_NAME
  DISCORD_WEBHOOK_URL
)

DOCKER_ENV_ARGS=()

if [ -z "$APP_IMAGE" ]; then
  echo "[ERRO] APP_IMAGE nao foi definido"
  exit 1
fi

MISSING_VARS=()
for var_name in "${REQUIRED_RUNTIME_VARS[@]}"; do
  if [ -z "${!var_name:-}" ]; then
    MISSING_VARS+=("$var_name")
  fi
done

if [ ${#MISSING_VARS[@]} -gt 0 ]; then
  echo "[ERRO] Variaveis obrigatorias ausentes: ${MISSING_VARS[*]}"
  exit 1
fi

for var_name in "${REQUIRED_RUNTIME_VARS[@]}"; do
  DOCKER_ENV_ARGS+=("-e" "$var_name")
done

echo "[INFO] Iniciando deploy do backend..."
echo "[INFO] Nova imagem: $APP_IMAGE"

PREV_IMAGE_ID=$(docker inspect --format='{{.Image}}' "$CONTAINER_NAME" 2>/dev/null || true)
echo "[INFO] Imagem anterior: ${PREV_IMAGE_ID:-nenhuma}"

rollback() {
  echo "[ERRO] Executando rollback..."
  docker rm -f "$CONTAINER_NAME" >/dev/null 2>&1 || true

  if [ -n "$PREV_IMAGE_ID" ]; then
    docker run -d --name "$CONTAINER_NAME" --restart always -p "${HOST_PORT}:${CONTAINER_PORT}" "${DOCKER_ENV_ARGS[@]}" "$PREV_IMAGE_ID"
  else
    echo "[ERRO] Sem imagem para rollback"
  fi
}

echo "[INFO] Pull da nova imagem..."
if ! docker pull "$APP_IMAGE"; then
  echo "[ERRO] Falha ao baixar imagem"
  exit 1
fi

if docker inspect "$CONTAINER_NAME" >/dev/null 2>&1; then
  docker stop "$CONTAINER_NAME"
  docker rm "$CONTAINER_NAME"
fi

if ! docker run -d --name "$CONTAINER_NAME" --restart always -p "${HOST_PORT}:${CONTAINER_PORT}" "${DOCKER_ENV_ARGS[@]}" "$APP_IMAGE"; then
  rollback
  exit 1
fi

for i in {1..30}; do
  if curl -fsS "$HEALTHCHECK_URL" >/dev/null; then
    echo "[INFO] Healthcheck OK"
    docker ps --filter "name=$CONTAINER_NAME"
    echo "[INFO] Deploy concluido com sucesso"
    exit 0
  fi
  sleep 2
done

echo "[ERRO] Healthcheck falhou em $HEALTHCHECK_URL"
rollback
exit 1

