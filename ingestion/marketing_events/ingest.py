from ingestion.job import IngestJob, IngestionConfig
import polars as pl


class MarketingEventsIngestJob(IngestJob):
    def __init__(self, s3_client):
        config = IngestionConfig(
            source_path="../../sources/marketing_events.csv",
            bronze_bucket="bronze/marketing-events",
            reader=pl.read_csv,
        )
        super().__init__(s3_client, config)

