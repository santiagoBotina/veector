import io
from datetime import datetime
from dataclasses import dataclass
from typing import Callable
import polars as pl
import pyarrow as pa
import pyarrow.parquet as pq


@dataclass
class IngestionConfig:
    source_path: str
    bronze_bucket: str
    reader: Callable
    batch_size: int = 100_000


class IngestJob:
    def __init__(self, s3_client, config: IngestionConfig):
        self.s3_client = s3_client
        self.config = config

    def run(self):
        """Extract and load in chunks, never holding full dataset in memory."""
        today = datetime.now()
        base_key = self._get_base_key(today)

        total = 0
        for i, batch in enumerate(self._read_batches()):
            s3_key = f"{base_key}/part-{i:04d}.parquet"
            self._upload_batch(batch, s3_key)
            total += batch.height
            print(f"Uploaded batch {i} ({batch.height} records) → {s3_key}")

        print(f"======== {self.config.source_path} — {total} total records ingested ========")

    def _read_batches(self):
        """Yield DataFrames in chunks."""
        source = self.config.source_path
        batch_size = self.config.batch_size

        if source.endswith(".csv"):
            reader = pl.read_csv_batched(source, batch_size=batch_size)
            while True:
                batches = reader.next_batches(1)
                if not batches:
                    break
                yield batches[0]

        elif source.endswith(".jsonl") or source.endswith(".ndjson"):
            # Polars has no native NDJSON batched reader, so scan lazily
            lf = pl.scan_ndjson(source)
            total = lf.select(pl.count()).collect().item()
            for offset in range(0, total, batch_size):
                yield lf.slice(offset, batch_size).collect()

    def _upload_batch(self, df: pl.DataFrame, s3_key: str):
        table = df.to_arrow()
        buffer = io.BytesIO()
        pq.write_table(table, buffer, compression="snappy")
        buffer.seek(0)
        self.s3_client.upload(buffer, s3_key)

    def _get_base_key(self, today: datetime) -> str:
        folder = self.config.bronze_bucket.strip("/")
        return f"{folder}/year={today.year}/month={today.month}/day={today.day}"

