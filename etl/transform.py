from dataclasses import dataclass
import time

import pandas as pd

from logger import get_run_id, logger


@dataclass
class PreparedData:
    body_parts: pd.DataFrame
    equipment: pd.DataFrame
    muscles: pd.DataFrame
    exercises: pd.DataFrame
    exercise_muscles: pd.DataFrame


def _clean_text(value: object) -> str | None:
    if pd.isna(value):
        return None
    text = str(value).strip()
    if not text or text.lower() == "nan":
        return None
    return text


def summarize_instructions(row: pd.Series, instruction_cols: list[str]) -> str:
    steps = []
    step_num = 1
    for column in instruction_cols:
        value = row[column]
        if pd.notna(value) and str(value).strip():
            steps.append(f"{step_num}. {str(value).strip()}")
            step_num += 1
    return "\n".join(steps)


def prepare_data(df: pd.DataFrame) -> PreparedData:
    """Consolida instruções e prepara os DataFrames relacionais do ETL."""
    start_time = time.perf_counter()
    logger.info(
        f"Iniciando transformação de {len(df)} registros",
        extra={"step": "transform", "run_id": get_run_id(), "total_rows": len(df)},
    )

    instruction_cols = [
        column for column in df.columns if column.startswith("instructions/")
    ]
    prepared_df = df.copy()
    prepared_df["instructions_sumarization"] = prepared_df.apply(
        summarize_instructions,
        axis=1,
        instruction_cols=instruction_cols,
    )

    secondary_cols = [
        column for column in prepared_df.columns
        if column.startswith("secondaryMuscles/")
    ]
    muscle_cols = ["target", *secondary_cols]
    raw_muscles = []
    for column in muscle_cols:
        raw_muscles.extend(prepared_df[column].dropna().unique().tolist())

    muscles = sorted({
        cleaned
        for muscle in raw_muscles
        if (cleaned := _clean_text(muscle)) is not None
    })

    body_parts = pd.DataFrame({
        "name": sorted(prepared_df["bodyPart"].dropna().unique())
    })
    equipment = pd.DataFrame({
        "name": sorted(prepared_df["equipment"].dropna().unique())
    })
    muscle_table = pd.DataFrame({"name": muscles})

    exercises = pd.DataFrame({
        "name": prepared_df["name"],
        "gif_url": prepared_df["gif_url"],
        "instructions_sumarization": prepared_df["instructions_sumarization"],
        "body_part_name": prepared_df["bodyPart"],
        "equipment_name": prepared_df["equipment"],
    })

    relations = []
    for _, row in prepared_df.iterrows():
        target = _clean_text(row["target"])
        if target is not None:
            relations.append({
                "exercise_name": row["name"],
                "exercise_gif_url": row["gif_url"],
                "muscle_name": target,
                "type": "principal",
            })

        for column in secondary_cols:
            muscle = _clean_text(row[column])
            if muscle is not None:
                relations.append({
                    "exercise_name": row["name"],
                    "exercise_gif_url": row["gif_url"],
                    "muscle_name": muscle,
                    "type": "secundario",
                })

    exercise_muscles = (
        pd.DataFrame(
            relations,
            columns=[
                "exercise_name",
                "exercise_gif_url",
                "muscle_name",
                "type",
            ],
        )
        .drop_duplicates(
            subset=["exercise_name", "exercise_gif_url", "muscle_name"],
            keep="first",
        )
        .reset_index(drop=True)
    )

    duration_ms = round((time.perf_counter() - start_time) * 1000, 2)
    logger.info(
        "Transformação concluída com sucesso",
        extra={
            "step": "transform",
            "run_id": get_run_id(),
            "total_rows": len(exercises),
            "duration_ms": duration_ms,
        },
    )

    return PreparedData(
        body_parts=body_parts,
        equipment=equipment,
        muscles=muscle_table,
        exercises=exercises,
        exercise_muscles=exercise_muscles,
    )
