import boto3
import os
from botocore.exceptions import NoCredentialsError

S3_URL = os.environ["S3_URL"]
AWS_ACCESS_KEY = os.environ["S3_ACCESS_KEY"]
AWS_SECRET_KEY = os.environ["S3_SECRET_KEY"]

class Client:
    def __init__(self):
        self.session = boto3.Session(
            aws_access_key_id=AWS_ACCESS_KEY,
            aws_secret_access_key=AWS_SECRET_KEY,
        )

        self.client = self.session.resource('s3', endpoint_url=S3_URL)

    def upload(self, bucket_name, file_path, file_name):
        try:
            self.client.Bucket(bucket_name).put_object(Key=file_name, Body=open(file_path, 'rb'))
            print(f"File {file_name} uploaded successfully to {bucket_name}.")
        except NoCredentialsError:
            print("Error: Invalid credentials.")

    def download_file(self, bucket_name, file_name, download_path):
        try:
            obj = self.client.Bucket(bucket_name).Object(file_name).get()
            with open(download_path, 'wb') as f:
                f.write(obj['Body'].read())
            print(f"File {file_name} downloaded successfully to {download_path}.")
        except ValueError as error:
            print(f"Error downloading file: {file_name} - Error: {error}.")


S3Client = Client()