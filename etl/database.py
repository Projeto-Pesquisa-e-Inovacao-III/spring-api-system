import os
from pathlib import Path
from typing import Any
from urllib.parse import quote_plus

import pandas as pd
from dotenv import load_dotenv
from sqlalchemy import Engine, create_engine, text


ENV_PATH = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(ENV_PATH)


def create_database_engine(connection_string: str | None = None) -> Engine:
    connection_string = connection_string or os.getenv("MYSQL_URL")

    if not connection_string:
        username = os.getenv("MYSQL_USERNAME")
        password = os.getenv("MYSQL_PASSWORD", "")
        host = os.getenv("MYSQL_HOST", "localhost")
        port = os.getenv("MYSQL_PORT", "3306")
        database = os.getenv("MYSQL_DATABASE", "spring_api_system")

        if username:
            encoded_user = quote_plus(username)
            encoded_password = quote_plus(password)
            connection_string = (
                f"mysql+pymysql://{encoded_user}:{encoded_password}"
                f"@{host}:{port}/{database}"
            )

    if not connection_string:
        raise RuntimeError(
            "Defina MYSQL_USERNAME e MYSQL_PASSWORD (ou MYSQL_URL) no arquivo .env "
            "antes de acessar o banco. "
            "Exemplo: mysql+pymysql://usuario:senha@localhost:3306/spring_api_system"
        )
    return create_engine(connection_string)


def run_sql(
    engine: Engine,
    query: str,
    params: dict[str, Any] | None = None,
) -> pd.DataFrame:
    return pd.read_sql_query(text(query), con=engine, params=params or {})
