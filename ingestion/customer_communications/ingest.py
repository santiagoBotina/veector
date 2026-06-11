from ingestion.job import IngestJob, IngestionConfig
import polars as pl


class CustomerCommunicationsIngestJob(IngestJob):
    def __init__(self, s3_client):
        config = IngestionConfig(
            source_path="../../sources/customer_communications.csv",
            bronze_bucket="bronze/customer-communications",
            reader=pl.read_csv,
        )
        super().__init__(s3_client, config)

