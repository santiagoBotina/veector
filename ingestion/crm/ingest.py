from ingestion.job import Job
import polars as pl

class CRMIngestJob(Job):
    def __init__(self, s3_client):
        # TODO: replace this path wih drive's --------
        self.dataset_path = '../../sources/crm_customers.jsonl'
        self.s3_client = s3_client

    def run(self):
        df = pl.read_json(self.dataset_path)
        print(df)


