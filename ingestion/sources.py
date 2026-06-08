import polars as pl
from ingestion.job import IngestionConfig

SOURCES = {
    "crm": IngestionConfig(
        source_path="../../sources/crm_customers.jsonl",
        bronze_bucket="bronze/crm",
        reader=pl.read_ndjson,
    ),
    "orders": IngestionConfig(
        source_path="../../sources/orders.csv",
        bronze_bucket="bronze/orders",
        reader=pl.read_csv,
    ),
    "marketing": IngestionConfig(
        source_path="../../sources/marketing_events.csv",
        bronze_bucket="bronze/marketing",
        reader=pl.read_csv,
    ),
    "support": IngestionConfig(
        source_path="../../sources/support_tickets.jsonl",
        bronze_bucket="bronze/support",
        reader=pl.read_ndjson,
    ),
    "communications": IngestionConfig(
        source_path="../../sources/customer_communications.csv",
        bronze_bucket="bronze/communications",
        reader=pl.read_csv,
    ),
}