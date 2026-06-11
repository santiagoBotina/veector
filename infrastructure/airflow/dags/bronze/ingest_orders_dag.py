from dags.bronze.factory import make_bronze_dag
from ingestion.job import IngestJob
from ingestion.sources import SOURCES

make_bronze_dag(
    dag_id="ingest_bronze_orders",
    job_class=IngestJob,
    source_name="orders",
    sources=SOURCES,
    schedule="@daily",
    tags=["ingestion", "bronze", "orders"],
)()