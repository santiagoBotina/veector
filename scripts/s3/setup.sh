#!/usr/bin/env bash

set -euo pipefail

MINIO_ALIAS="local"
MINIO_ENDPOINT="http://minio:9000"

MINIO_ROOT_USER="${S3_ACCESS_KEY:-admin}"
MINIO_ROOT_PASSWORD="${S3_SECRET_KEY:-password123}"

BUCKET=${LAKEHOUSE_ENTRY_BUCKET}

echo "Waiting for MinIO..."

until mc alias set \
    "$MINIO_ALIAS" \
    "$MINIO_ENDPOINT" \
    "$MINIO_ROOT_USER" \
    "$MINIO_ROOT_PASSWORD" >/dev/null 2>&1
do
    sleep 2
done

echo "MinIO available."

echo "Creating bucket if it does not exist..."

mc mb --ignore-existing "${MINIO_ALIAS}/${BUCKET}"

echo "MinIO lakehouse initialized successfully."
