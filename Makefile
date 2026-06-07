# Run
run:
	docker compose up -d

stop:
	docker compose down

# --- Airflow
airflow-destroy:
	cd ./infrastructure/airflow && docker compose down --volume --rmi all

# -- S3 (minio)
minio-run:
	cd ./infrastructure/s3 && docker compose up -d

