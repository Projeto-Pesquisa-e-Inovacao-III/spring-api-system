#!/usr/bin/env bash
set -euo pipefail

APP_IMAGE="${APP_IMAGE:-}"
CONTAINER_NAME="${CONTAINER_NAME:-spring-api-system}"
HOST_PORT="${HOST_PORT:-8080}"
CONTAINER_PORT="${CONTAINER_PORT:-8080}"
HEALTHCHECK_URL="${HEALTHCHECK_URL:-http://localhost:${HOST_PORT}/}"
JWT_KEYS_HOST_DIR="${JWT_KEYS_HOST_DIR:-/opt/spring-api-system/keys}"
JWT_KEYS_CONTAINER_DIR="${JWT_KEYS_CONTAINER_DIR:-/run/keys}"
JWT_PUBLIC_KEY="${JWT_PUBLIC_KEY:-file:${JWT_KEYS_CONTAINER_DIR}/public.pem}"
JWT_PRIVATE_KEY="${JWT_PRIVATE_KEY:-file:${JWT_KEYS_CONTAINER_DIR}/private.pem}"

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
  JWT_PUBLIC_KEY
  JWT_PRIVATE_KEY
  RABBITMQ_HOST
  RABBITMQ_PORT
  RABBITMQ_USERNAME
  RABBITMQ_PASSWORD
  RABBITMQ_VIRTUAL_HOST
  RABBITMQ_HMAC_SECRET
  SPRING_REDIS_HOST
  SPRING_REDIS_PORT
  SPRING_REDIS_PASSWORD
  STORAGE_TYPE
  STORAGE_S3_PATH
  AWS_REGION
  AWS_S3_BUCKET
  AWS_S3_ENDPOINT
  AWS_S3_PATH_STYLE
  INIT_EMAIL
  INIT_PASSWORD
)

DOCKER_ENV_ARGS=()
DOCKER_VOLUME_ARGS=()

if [ -z "$APP_IMAGE" ]; then
  echo "[ERRO] APP_IMAGE nao foi definido"
  exit 1
fi

if [ ! -f "${JWT_KEYS_HOST_DIR}/private.pem" ] || [ ! -f "${JWT_KEYS_HOST_DIR}/public.pem" ]; then
  echo "[ERRO] Chaves JWT nao encontradas em ${JWT_KEYS_HOST_DIR}"
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

DOCKER_VOLUME_ARGS+=("-v" "${JWT_KEYS_HOST_DIR}:${JWT_KEYS_CONTAINER_DIR}:ro")

echo "[INFO] Iniciando deploy do backend..."
echo "[INFO] Nova imagem: $APP_IMAGE"

PREV_IMAGE_ID=$(docker inspect --format='{{.Image}}' "$CONTAINER_NAME" 2>/dev/null || true)
echo "[INFO] Imagem anterior: ${PREV_IMAGE_ID:-nenhuma}"

rollback() {
  echo "[ERRO] Executando rollback..."
  docker rm -f "$CONTAINER_NAME" >/dev/null 2>&1 || true

  if [ -n "$PREV_IMAGE_ID" ]; then
    docker run -d --name "$CONTAINER_NAME" --restart always -p "${HOST_PORT}:${CONTAINER_PORT}" "${DOCKER_VOLUME_ARGS[@]}" "${DOCKER_ENV_ARGS[@]}" "$PREV_IMAGE_ID"
  else
    echo "[ERRO] Sem imagem para rollback"
  fi
}

cleanup_previous_image() {
  CURRENT_IMAGE_ID=$(docker inspect --format='{{.Image}}' "$CONTAINER_NAME" 2>/dev/null || true)

  if [ -z "$PREV_IMAGE_ID" ]; then
    return
  fi

  if [ "$PREV_IMAGE_ID" = "$CURRENT_IMAGE_ID" ]; then
    echo "[INFO] Imagem anterior e igual a atual. Nenhuma limpeza necessaria."
  elif docker image rm "$PREV_IMAGE_ID" >/dev/null 2>&1; then
    echo "[INFO] Imagem anterior removida com sucesso: $PREV_IMAGE_ID"
  else
    echo "[INFO] Nao foi possivel remover a imagem anterior (pode estar em uso). Seguindo deploy."
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

if ! docker run -d --name "$CONTAINER_NAME" --restart always -p "${HOST_PORT}:${CONTAINER_PORT}" "${DOCKER_VOLUME_ARGS[@]}" "${DOCKER_ENV_ARGS[@]}" "$APP_IMAGE"; then
  rollback
  exit 1
fi

for i in {1..30}; do
  if curl -fsS "$HEALTHCHECK_URL" >/dev/null; then
    echo "[INFO] Healthcheck OK"
    docker ps --filter "name=$CONTAINER_NAME"
    cleanup_previous_image
    echo "[INFO] Deploy concluido com sucesso"
    exit 0
  fi
  sleep 2
done

echo "[ERRO] Healthcheck falhou em $HEALTHCHECK_URL"
rollback
exit 1
