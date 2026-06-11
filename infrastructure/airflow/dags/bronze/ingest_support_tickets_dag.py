from dags.bronze.factory import make_bronze_dag
from ingestion.job import IngestJob
from ingestion.sources import SOURCES

make_bronze_dag(
    dag_id="ingest_bronze_support_tickets",
    job_class=IngestJob,
    source_name="support",
    sources=SOURCES,
    schedule="@daily",
    tags=["ingestion", "bronze", "support_tickets"],
)()