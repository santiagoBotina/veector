from ingestion.job import IngestJob, IngestionConfig
import polars as pl


class OrdersIngestJob(IngestJob):
    def __init__(self, s3_client):
        config = IngestionConfig(
            source_path="../../sources/orders.csv",
            bronze_bucket="bronze/orders",
            reader=pl.read_csv,
        )
        super().__init__(s3_client, config)

