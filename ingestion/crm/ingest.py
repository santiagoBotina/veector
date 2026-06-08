from ingestion.job import IngestJob, IngestionConfig
import polars as pl


class CRMIngestJob(IngestJob):
    def __init__(self, s3_client):
        config = IngestionConfig(
            source_path="../../sources/crm_customers.jsonl",
            bronze_bucket="bronze/crm",
            reader=pl.read_ndjson,
        )
        super().__init__(s3_client, config)

