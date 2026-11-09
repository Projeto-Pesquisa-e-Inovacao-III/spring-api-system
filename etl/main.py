import argparse
from pathlib import Path
import time
import uuid

from database import create_database_engine
from extract import read_exercises_csv
from load import insert_data_to_mysql
from logger import init_run_id, logger
from transform import prepare_data


PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_CSV_PATH = PROJECT_ROOT / "data" / "exercises(1).csv"


def run_pipeline(csv_path: str | Path) -> str:
    run_id = init_run_id(str(uuid.uuid4()))
    start_time = time.perf_counter()
    logger.info(
        f"Iniciando execução do pipeline ETL (run_id={run_id})",
        extra={"step": "pipeline", "run_id": run_id},
    )

    try:
        raw_data = read_exercises_csv(csv_path)
        prepared_data = prepare_data(raw_data)
        engine = create_database_engine()
        try:
            insert_data_to_mysql(engine, prepared_data)
        finally:
            engine.dispose()

        duration_ms = round((time.perf_counter() - start_time) * 1000, 2)
        logger.info(
            f"Pipeline ETL finalizado com sucesso (run_id={run_id})",
            extra={
                "step": "pipeline",
                "run_id": run_id,
                "duration_ms": duration_ms,
            },
        )
        return run_id
    except Exception as exc:
        duration_ms = round((time.perf_counter() - start_time) * 1000, 2)
        logger.error(
            f"Falha na execução do pipeline ETL (run_id={run_id}): {exc}",
            exc_info=True,
            extra={
                "step": "pipeline",
                "run_id": run_id,
                "duration_ms": duration_ms,
                "error_type": type(exc).__name__,
            },
        )
        raise


def main() -> None:
    parser = argparse.ArgumentParser(description="Executa o ETL de exercícios.")
    parser.add_argument(
        "csv_path",
        nargs="?",
        default=DEFAULT_CSV_PATH,
        type=Path,
    )
    args = parser.parse_args()
    run_pipeline(args.csv_path)


if __name__ == "__main__":
    main()
