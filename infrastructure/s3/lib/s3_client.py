import boto3
import os
from botocore.exceptions import NoCredentialsError

S3_URL = os.environ["S3_URL"]
AWS_ACCESS_KEY = os.environ["S3_ACCESS_KEY"]
AWS_SECRET_KEY = os.environ["S3_SECRET_KEY"]

BASE_LAKEHOUSE_BUCKET = os.environ['LAKEHOUSE_ENTRY_BUCKET']

class Client:
    def __init__(self):
        self.session = boto3.Session(
            aws_access_key_id=AWS_ACCESS_KEY,
            aws_secret_access_key=AWS_SECRET_KEY,
        )

        self.client = self.session.resource('s3', endpoint_url=S3_URL)

    def upload(self, buffer, s3_key):
        try:
            print(f"Object path: {s3_key}")
            self.client.Bucket(BASE_LAKEHOUSE_BUCKET).put_object(Key=s3_key, Body=buffer)
            print(f"File uploaded successfully to s3://{BASE_LAKEHOUSE_BUCKET}/{s3_key}")
        except NoCredentialsError:
            print("Error: Invalid credentials.")
        except Exception as e:
            print(f"Error uploading file: {e}")
            raise

    def download_file(self, bucket_name, file_name, download_path):
        try:
            obj = self.client.Bucket(bucket_name).Object(file_name).get()
            with open(download_path, 'wb') as f:
                f.write(obj['Body'].read())
            print(f"File {file_name} downloaded successfully to {download_path}.")
        except ValueError as error:
            print(f"Error downloading file: {file_name} - Error: {error}.")


S3Client = Client()