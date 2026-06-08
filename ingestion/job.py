import io
from datetime import datetime
from dataclasses import dataclass
import polars as pl


@dataclass
class IngestionConfig:
    source_path: str
    bronze_bucket: str
    reader: callable


class IngestJob:
    def __init__(self, s3_client, config: IngestionConfig):
        self.s3_client = s3_client
        self.config = config

    def extract(self) -> pl.DataFrame:
        df = self.config.reader(self.config.source_path)
        print(f"======== Extracted from {self.config.source_path} - {df.height} records ========")
        return df

    def load(self, records: list) -> None:
        s3_key = self._get_s3_key()
        df = pl.from_dicts(records)

        buffer = io.BytesIO()
        df.write_parquet(buffer)
        buffer.seek(0)

        self.s3_client.upload(buffer, s3_key)

    def _get_s3_key(self) -> str:
        today = datetime.now()
        folder = self.config.bronze_bucket.strip("/")
        return f"{folder}/year={today.year}/month={today.month}/day={today.day}/data.parquet"
