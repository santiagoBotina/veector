from ingestion.job import IngestJob, IngestionConfig
import polars as pl


class SupportTicketsIngestJob(IngestJob):
    def __init__(self, s3_client):
        config = IngestionConfig(
            source_path="../../sources/support_tickets.jsonl",
            bronze_bucket="bronze/support-tickets",
            reader=pl.read_ndjson,
        )
        super().__init__(s3_client, config)

