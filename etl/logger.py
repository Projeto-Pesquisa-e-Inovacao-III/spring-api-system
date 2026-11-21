from datetime import datetime, timezone
import json
import logging
import os
import sys
import traceback
import urllib.request
import uuid
from typing import Any

_current_run_id: str = str(uuid.uuid4())


def init_run_id(run_id: str | None = None) -> str:
    global _current_run_id
    _current_run_id = run_id or str(uuid.uuid4())
    return _current_run_id


def get_run_id() -> str:
    return _current_run_id


class EtlJsonFormatter(logging.Formatter):
    """Formats Python log records as structured JSON for Loki & console output."""

    def __init__(self, env: str = "dev") -> None:
        super().__init__()
        self.env = env

    def format(self, record: logging.LogRecord) -> str:
        payload: dict[str, Any] = {
            "timestamp": datetime.now(timezone.utc).isoformat(),
            "level": record.levelname,
            "service_name": "csf-etl",
            "source": "etl",
            "env": self.env,
            "run_id": getattr(record, "run_id", None) or get_run_id(),
            "step": getattr(record, "step", "pipeline"),
            "message": record.getMessage(),
        }

        for extra_field in ("table", "inserted_rows", "total_rows", "duration_ms", "error_type"):
            val = getattr(record, extra_field, None)
            if val is not None:
                payload[extra_field] = val

        if record.exc_info:
            exc_type, _, _ = record.exc_info
            if exc_type is not None and "error_type" not in payload:
                payload["error_type"] = exc_type.__name__
            payload["traceback"] = "".join(traceback.format_exception(*record.exc_info))

        return json.dumps(payload, ensure_ascii=False)


class SafeLokiHttpHandler(logging.Handler):
    """Pushes JSON-formatted log lines to Loki via python-logging-loki or HTTP fallback.

    Never raises exceptions if Loki is unreachable; logs a one-time warning and continues.
    """

    def __init__(self, loki_url: str, env: str = "dev") -> None:
        super().__init__()
        self.loki_url = loki_url
        self.env = env
        self._warned_unreachable = False
        self._delegate: logging.Handler | None = None

        try:
            import logging_loki  # type: ignore

            self._delegate = logging_loki.LokiHandler(
                url=loki_url,
                tags={
                    "service_name": "csf-etl",
                    "env": env,
                    "source": "etl",
                },
                version="1",
            )
        except Exception:
            self._delegate = None

    def emit(self, record: logging.LogRecord) -> None:
        if not self.loki_url:
            return

        formatted_line = self.format(record)
        try:
            body = json.dumps(
                {
                    "streams": [
                        {
                            "stream": {
                                "service_name": "csf-etl",
                                "env": self.env,
                                "level": record.levelname,
                                "source": "etl",
                            },
                            "values": [
                                [str(int(datetime.now(timezone.utc).timestamp() * 1_000_000_000)), formatted_line]
                            ],
                        }
                    ]
                }
            ).encode("utf-8")
            req = urllib.request.Request(
                self.loki_url,
                data=body,
                headers={"Content-Type": "application/json"},
                method="POST",
            )
            with urllib.request.urlopen(req, timeout=1.5):
                pass
        except Exception as exc:
            if not self._warned_unreachable:
                self._warned_unreachable = True
                sys.stderr.write(
                    f"[CSF-ETL] Warning: Loki unreachable at {self.loki_url} ({exc}). "
                    "Continuing with console and local file logging.\n"
                )


def get_etl_logger(name: str = "csf.etl") -> logging.Logger:
    logger = logging.getLogger(name)
    if logger.handlers:
        return logger

    logger.setLevel(logging.INFO)
    logger.propagate = False

    env = os.getenv("APP_ENV", "dev")
    loki_url = os.getenv("LOKI_URL", "http://localhost:3100/loki/api/v1/push").strip()

    formatter = EtlJsonFormatter(env=env)

    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)

    if loki_url:
        loki_handler = SafeLokiHttpHandler(loki_url=loki_url, env=env)
        loki_handler.setFormatter(formatter)
        logger.addHandler(loki_handler)
    else:
        logger.warning("LOKI_URL is unset; continuing with Console and local file logging.")

    return logger


logger = get_etl_logger()
