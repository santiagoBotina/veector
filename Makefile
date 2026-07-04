up:
	docker compose \
		-f infrastructure/airflow/compose.yaml \
		-f infrastructure/s3/compose.yml \
		-f infrastructure/spark/compose.yml \
		--env-file .env \
		up -d

down:
	docker compose \
		-f infrastructure/airflow/compose.yaml \
		-f infrastructure/s3/compose.yml \
		-f infrastructure/spark/compose.yml \
		--env-file .env \
		down --volumes --remove-orphans

# --- Airflow
airflow-destroy:
	docker compose -f infrastructure/airflow  down --volume --rmi all

# -- S3 (minio)
s3-run:
	docker compose -f infrastructure/s3/compose.yml --env-file .env up -d
