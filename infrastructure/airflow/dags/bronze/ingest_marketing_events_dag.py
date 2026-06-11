from dags.bronze.factory import make_bronze_dag
from ingestion.job import IngestJob
from ingestion.sources import SOURCES

make_bronze_dag(
    dag_id="ingest_bronze_marketing_events",
    job_class=IngestJob,
    source_name="marketing",
    sources=SOURCES,
    schedule="@daily",
    tags=["ingestion", "bronze", "marketing_events"],
)()