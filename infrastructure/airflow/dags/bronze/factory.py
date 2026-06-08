from datetime import datetime
from airflow.decorators import dag, task
from typing import Type, Dict

def make_bronze_dag(
        dag_id: str,
        job_class: Type,
        source_name: str,
        sources: Dict,
        schedule: str = "@daily",
        tags: list = [],
):
    @dag(
        dag_id=dag_id,
        start_date=datetime(2024, 1, 1),
        schedule=schedule,
        catchup=False,
        tags=tags,
    )
    def pipeline():

        @task()
        def extract() -> list:
            from infrastructure.s3.lib.s3_client import S3Client
            job = job_class(S3Client, sources[source_name])
            return job.extract().to_dicts()

        @task()
        def load(records: list) -> None:
            from infrastructure.s3.lib.s3_client import S3Client
            job = job_class(S3Client, sources[source_name])
            job.load(records)

        extracted_records = extract()
        load(extracted_records)

    return pipeline
