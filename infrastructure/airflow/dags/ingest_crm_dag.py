from datetime import datetime
from airflow.decorators import dag

from infrastructure.s3.lib.s3_client import S3Client
from ingestion.crm.ingest import CRMIngestJob

today = datetime.now()

year = today.year
month = today.month
day = today.day

@dag(
    dag_id="ingest_crm_dag",
    start_date=datetime(year, month, day),
    schedule="@daily",
    catchup=False,
    tags=["ingestion"],
)
def ingest_crm():
    CRMIngestJob(S3Client).run()


ingest_crm()
