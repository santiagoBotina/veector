from datetime import datetime
from pathlib import Path
from airflow.decorators import dag
from airflow.operators.bash import BashOperator

PROJECT_ROOT = Path("/opt/airflow/project")
ENV_PATH = PROJECT_ROOT / ".env"
JAR_PATH = PROJECT_ROOT / "processing/target/out/jvm/scala-2.12.18/processing/veector-processing.jar"


def _load_env() -> dict:
    env = {}
    if not ENV_PATH.exists():
        return env
    for line in ENV_PATH.read_text().splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, _, value = line.partition("=")
        env[key.strip()] = value.strip().strip("\"'")
    return env


def make_silver_dag(
    dag_id: str,
    spark_class: str,
    jar_path: str = str(JAR_PATH),
    schedule: str = "@daily",
    tags: list = None,
):
    env = _load_env()
    s3_endpoint = env.get("S3_URL", "http://minio:9000")
    s3_access_key = env.get("S3_ACCESS_KEY", "admin")
    s3_secret_key = env.get("S3_SECRET_KEY", "password123")

    @dag(
        dag_id=dag_id,
        start_date=datetime(2024, 1, 1),
        schedule=schedule,
        catchup=False,
        tags=tags or [],
    )
    def pipeline():
        BashOperator(
            task_id="scala_job_run",
            bash_command=f"""
            spark-submit \
              --master spark://spark-master:7077 \
              --conf spark.hadoop.fs.s3a.endpoint={s3_endpoint} \
              --conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
              --conf spark.hadoop.fs.s3a.access.key={s3_access_key} \
              --conf spark.hadoop.fs.s3a.secret.key={s3_secret_key} \
              --conf spark.hadoop.fs.s3a.path.style.access=true \
              --class {spark_class} \
              {jar_path}
            """
        )

    return pipeline
