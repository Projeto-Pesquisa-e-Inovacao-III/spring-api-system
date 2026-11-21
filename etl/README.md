# etl-data-exercises

Pipeline de carregamento, tratamento, modelagem e ingestão de dados de exercícios físicos. O objetivo final é disponibilizar dados estruturados para uma aplicação de geração de treinos apoiada por LLM.

## Objetivo

O projeto transforma o arquivo CSV de exercícios em dados relacionais prontos para consulta:

- carrega e inspeciona os dados com Pandas;
- verifica duplicidades e valores disponíveis;
- consolida as instruções de cada exercício;
- separa equipamentos, partes do corpo e músculos (unificando `target` e `secondaryMuscles/*`) em tabelas de referência;
- cria a relação muitos-para-muitos entre exercícios e músculos na tabela associativa `exercise_muscle`, diferenciando o papel de cada músculo na coluna `type` (`principal` ou `secundario`);
- cria as tabelas no MySQL e insere os dados respeitando as chaves estrangeiras;
- executa consultas para analisar os exercícios por parte do corpo, músculo (principal ou secundário) e equipamento.

## Estado atual

O fluxo está sendo validado em um notebook, em [notebooks/tratamento_pandas_data.ipynb](notebooks/tratamento_pandas_data.ipynb). O notebook funciona como um protótipo executável para validar as regras de tratamento, o modelo relacional e as consultas.

O fluxo também está disponível em módulos Python na raiz, mantendo a mesma lógica do notebook:

- [main.py](main.py): orquestra leitura, transformação e carga na ordem correta;
- [extract.py](extract.py): lê o CSV e retorna o DataFrame bruto;
- [transform.py](transform.py): consolida instruções e prepara os DataFrames relacionais;
- [load.py](load.py): insere dimensões, exercícios e relações, registra logs e contém as consultas analíticas;
- [database.py](database.py): cria o engine SQLAlchemy e centraliza a execução de consultas;
- [logger.py](logger.py): gera logs estruturados em JSON com `run_id` e envia métricas/eventos do ETL para o Grafana Loki (`LOKI_URL`) sem interromper a execução caso o serviço esteja indisponível.

Os módulos não executam o ETL ao serem importados. A execução começa somente pelo `main.py`.

## Estrutura

```text
.
├── data/
│   └── exercises(1).csv
├── database.py
├── extract.py
├── load.py
├── logger.py
├── main.py
├── notebooks/
│   └── tratamento_pandas_data.ipynb
├── transform.py
├── requirements.txt
└── README.md
```

## Pré-requisitos

- Python 3.10 ou superior;
- MySQL Server, caso queira executar a criação das tabelas, a carga e as consultas;
- VS Code com a extensão Jupyter ou Jupyter Notebook instalado;
- Git, caso o projeto seja clonado do repositório.

## Configuração do ambiente virtual

No PowerShell, a partir da pasta raiz do projeto:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -r requirements.txt
```

Se o PowerShell bloquear a ativação de scripts, execute uma vez, com o escopo do usuário:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

Depois, ative novamente o ambiente virtual.

## Bibliotecas utilizadas

As dependências estão em `requirements.txt`:

- `pandas`: leitura, limpeza e transformação dos dados;
- `SQLAlchemy`: criação da conexão e persistência dos DataFrames;
- `PyMySQL`: driver Python para conexão com o MySQL;
- `python-logging-loki`: envio de logs estruturados do ETL para o Grafana Loki;
- `jupyter` e `ipykernel`: execução do notebook e seleção do ambiente no VS Code.

## Como abrir e executar o notebook

1. Abra a pasta do projeto no VS Code.
2. Selecione o interpretador `.venv` com `Python: Select Interpreter`.
3. Abra `notebooks/tratamento_pandas_data.ipynb`.
4. Selecione o kernel Python associado ao `.venv`.
5. Execute as células na ordem, começando pela importação e leitura do CSV.

### Atenção ao caminho do CSV

O notebook atualmente lê o arquivo usando apenas `exercises(1).csv`. Portanto, para executar o protótipo sem alterar o código, o diretório de trabalho da sessão precisa conter esse arquivo. Caso o CSV esteja apenas em `data/`, configure o diretório de trabalho do notebook para `data/` ou disponibilize uma cópia do arquivo no diretório usado pela sessão.

## Como executar os módulos Python

Configure a conexão sem gravar credenciais nos arquivos:

```powershell
$env:MYSQL_URL = "mysql+pymysql://usuario:senha@localhost:3306/spring_api_system"
```

Depois, a partir da raiz do projeto, execute:

```powershell
python etl\main.py
```

Também é possível informar outro CSV:

```powershell
python etl\main.py "caminho\para\outra-base.csv"
```

O critério de reutilização é o mesmo do notebook: dimensões são localizadas por `name`, exercícios por `(name, gif_url)` e relações por `(exercise_id, muscle_id)`. Portanto, executar a mesma base novamente não insere exercícios ou relações duplicados.

## Configuração do MySQL

Para executar a parte de banco:

1. Inicie o MySQL Server.
2. Crie o banco de dados que será usado pelo projeto.
3. Crie as tabelas pelo backend Java, incluindo `AUTO_INCREMENT` para os IDs e `UNIQUE` para os nomes das tabelas de referência. O nome de `exercise` não é único.
4. Configure usuário, senha, host, porta e nome do banco na conexão.
5. Execute a função de inserção depois que as tabelas forem criadas.
6. Execute as consultas analíticas após a carga dos dados.

A carga é executada em uma transação e pode ser repetida sem reinserir registros que já existam. O notebook grava `etl_summary.log` com a quantidade de linhas novas por tabela e `etl_errors.log` com os detalhes de falhas. Em caso de erro, a transação é revertida.

O fluxo de inserção segue esta ordem. Os IDs das tabelas são gerados pelo banco; o notebook consulta esses IDs depois da carga das tabelas de referência para preencher as FKs dos exercícios e da tabela associativa.

### Reutilização de registros e IDs

O ETL não gera IDs localmente e não cria um novo registro quando o nome já existe:

- se uma parte do corpo, equipamento ou músculo já existir, o ETL reutiliza o `id` retornado pelo banco;
- se o valor ainda não existir, ele é inserido e o novo `id` gerado pelo banco é utilizado;
- se o exercício já existir, o ETL reutiliza o `id` existente;
- se a relação entre exercício e músculo já existir, ela não é inserida novamente.

Essa verificação é feita pelo campo `name` nas tabelas de referência (`body_part`, `equipment` e `muscle`), pelo par `(name, gif_url)` na tabela `exercise` e pelo par `(exercise_id, muscle_id)` na tabela associativa `exercise_muscle`. Exercícios podem ter o mesmo nome quando possuem URLs de GIF diferentes. A tabela associativa mantém sua chave primária composta `(exercise_id, muscle_id)` e armazena a coluna `type` (`'principal'` para o músculo vindo da coluna `target` do CSV e `'secundario'` para os músculos vindos das colunas `secondaryMuscles/*`). Caso um mesmo músculo apareça simultaneamente em `target` e em `secondaryMuscles/*` para o mesmo exercício, prevalece a classificação `'principal'`. Os valores textuais devem ser normalizados de forma consistente, pois `chest`, `Chest` e `chest ` são valores diferentes para uma comparação literal.

```text
body_part, equipment e muscle
					 ↓
				 exercise
					 ↓
			  exercise_muscle
```

## Diagrama final do banco

O banco será organizado com `exercise` como tabela principal. Partes do corpo e equipamentos possuem relação de um para muitos com exercícios. Todos os músculos (tanto o músculo-alvo `target` quanto os músculos secundários) ficam unificados na tabela `muscle`, relacionando-se com `exercise` por meio da tabela associativa `exercise_muscle`, que identifica na coluna `type` se o vínculo é `'principal'` ou `'secundario'`.

```mermaid
erDiagram
	BODY_PART ||--o{ EXERCISE : classifica
	EQUIPMENT ||--o{ EXERCISE : utiliza
	EXERCISE ||--o{ EXERCISE_MUSCLE : possui
	MUSCLE ||--o{ EXERCISE_MUSCLE : participa

	BODY_PART {
		BIGINT id PK
		VARCHAR name UK
	}

	EQUIPMENT {
		BIGINT id PK
		VARCHAR name UK
	}

	MUSCLE {
		BIGINT id PK
		VARCHAR name UK
	}

	EXERCISE {
		BIGINT id PK
		VARCHAR name
		VARCHAR gif_url
		LONGTEXT instructions_sumarization
		BIGINT fk_body_part FK
		BIGINT fk_equipment FK
	}

	EXERCISE_MUSCLE {
		BIGINT exercise_id PK, FK
		BIGINT muscle_id PK, FK
		VARCHAR type
	}
```

### Regras do relacionamento

- `body_part.id` é referenciada por `exercise.fk_body_part`.
- `equipment.id` é referenciada por `exercise.fk_equipment`.
- `exercise.id` e `muscle.id` formam a chave primária composta `(exercise_id, muscle_id)` da tabela associativa `exercise_muscle`.
- `exercise_muscle.type` indica o papel do músculo no exercício, aceitando apenas `'principal'` (1 por exercício, oriundo de `target`) ou `'secundario'` (0 ou mais por exercício, oriundos de `secondaryMuscles/*`).
- Um exercício pode possuir 1 músculo principal e vários músculos secundários.
- Um mesmo músculo pode atuar como principal em alguns exercícios e como secundário em outros.
- A exclusão de um exercício ou músculo remove suas relações correspondentes na tabela associativa.

## Estrutura em SQL (DDL)

Schema MySQL compatível com o pipeline ETL e com o mapeamento JPA (`BIGINT` para IDs, `LONGTEXT` para instruções):

```sql
CREATE TABLE IF NOT EXISTS body_part (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_body_part_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_equipment_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS muscle (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_muscle_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS exercise (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    gif_url VARCHAR(255) NULL,
    instructions_sumarization LONGTEXT NULL,
    fk_body_part BIGINT NULL,
    fk_equipment BIGINT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_exercise_name_gif UNIQUE (name, gif_url),
    CONSTRAINT fk_exercise_body_part FOREIGN KEY (fk_body_part) REFERENCES body_part (id),
    CONSTRAINT fk_exercise_equipment FOREIGN KEY (fk_equipment) REFERENCES equipment (id)
);

CREATE TABLE IF NOT EXISTS exercise_muscle (
    exercise_id BIGINT NOT NULL,
    muscle_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    PRIMARY KEY (exercise_id, muscle_id),
    CONSTRAINT fk_exercise_muscle_exercise FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE,
    CONSTRAINT fk_exercise_muscle_muscle FOREIGN KEY (muscle_id) REFERENCES muscle (id) ON DELETE CASCADE,
    CONSTRAINT chk_exercise_muscle_type CHECK (type IN ('principal', 'secundario'))
);
```

Não versionar senhas, tokens ou outras credenciais. Para o código definitivo, prefira variáveis de ambiente ou um gerenciador de segredos em vez de credenciais escritas no notebook.

## Execução por linha de comando

Com o ambiente virtual ativado, também é possível iniciar o Jupyter a partir da raiz do projeto:

```powershell
jupyter notebook
```

Depois, abra o notebook pela interface do Jupyter e selecione o kernel `.venv`.

## Limpeza das tabelas

O notebook [notebooks/clear_db.ipynb](notebooks/clear_db.ipynb) remove todos os dados das tabelas do ETL. A operação é destrutiva: faça um backup antes de executá-la.

Configure a mesma variável usada pelo ETL:

Para remover as tabelas e recriá-las do zero, use também o notebook [notebooks/drop_tables.ipynb](notebooks/drop_tables.ipynb). Ele remove primeiro `exercise_muscle`, depois as tabelas principais, sem usar `CASCADE`. Depois execute [notebooks/create_tables.ipynb](notebooks/create_tables.ipynb): as tabelas serão recriadas com IDs `AUTO_INCREMENT`.

```powershell
$env:MYSQL_URL = 'mysql+pymysql://usuario:senha@localhost:3306/spring_api_system'
```

No notebook, execute as células de configuração e remova o comentário de `clear_database(confirm=True)` somente após confirmar a limpeza. A função usa `DELETE`, respeita a ordem das chaves estrangeiras, registra a quantidade de linhas removidas e executa tudo em uma transação.

## Criação das tabelas

O notebook [notebooks/create_tables.ipynb](notebooks/create_tables.ipynb) cria o schema MySQL esperado pelo ETL. Ele usa `CREATE TABLE IF NOT EXISTS`, não apaga dados existentes e exige `create_tables(confirm=True)` para executar.

As tabelas usam IDs gerados pelo banco (`AUTO_INCREMENT`), nomes únicos nas tabelas de referência e as chaves estrangeiras da tabela `exercise_muscle` (incluindo a coluna `type`). Execute este notebook antes do ETL, usando a mesma variável `MYSQL_URL`.

## Próximos passos

- extrair o tratamento do notebook para classes e módulos Python;
- criar testes unitários para limpeza, modelagem e relacionamentos;
- centralizar a configuração do banco em variáveis de ambiente;
- corrigir o caminho do CSV para uma configuração independente do diretório de execução;
- automatizar a execução do ETL e a validação da carga;
- integrar os dados tratados à aplicação responsável pela geração de treinos.
