# Setup
setup:
	docker compose up -d

# --- Airflow
airflow-destroy:
	cd ./infrastructure/airflow && docker compose down --volume --rmi all

# -- S3 (minio)
minio-run:
	cd ./infrastructure/s3 && docker compose up -d

