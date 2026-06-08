from dags.bronze.factory import make_bronze_dag
from ingestion.job import IngestJob
from ingestion.sources import SOURCES

make_bronze_dag(
    dag_id="ingest_bronze_crm",
    job_class=IngestJob,
    source_name="crm",
    sources=SOURCES,
    schedule="@daily",
    tags=["ingestion", "bronze", "crm"],
)()