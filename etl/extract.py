from pathlib import Path
import time

import pandas as pd

from logger import get_run_id, logger


def read_exercises_csv(filename: str | Path) -> pd.DataFrame:
    """Lê a base de exercícios sem aplicar transformações."""
    start_time = time.perf_counter()
    logger.info(
        f"Iniciando extração do arquivo CSV: {filename}",
        extra={"step": "extract", "run_id": get_run_id()},
    )
    data = pd.read_csv(filename)
    renamed = data.rename(columns={"gifUrl": "gif_url"})
    duration_ms = round((time.perf_counter() - start_time) * 1000, 2)
    logger.info(
        f"Extração concluída: {len(renamed)} linhas lidas de {filename}",
        extra={
            "step": "extract",
            "run_id": get_run_id(),
            "total_rows": len(renamed),
            "duration_ms": duration_ms,
        },
    )
    return renamed
