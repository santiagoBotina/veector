# Veector — AI-Powered Customer Intelligence Platform

Veector is an enterprise-grade **Customer 360 Data Platform** that consolidates customer information from multiple operational systems into unified customer profiles for analytics, reporting, marketing optimization, customer support insights, and business intelligence.

Built on a **Lakehouse Architecture** with a **Medallion (Bronze/Silver/Gold)** data pipeline, it simulates a realistic e-commerce company operating across North America and Latin America with ~100K customers, 1M+ orders, and millions of marketing and support interactions.

---

## Business Context

The platform serves an online retailer selling consumer electronics, accessories, and smart home products. The organization uses several disconnected systems:

| System    | Purpose                    |
|-----------|----------------------------|
| CRM       | Customer management        |
| Orders    | Sales transactions         |
| Marketing | Campaign & engagement tracking |
| Support   | Customer service operations |

Leadership uses this platform to answer questions such as:

- What is the lifetime value of each customer?
- Which marketing campaigns drive the highest revenue?
- Which customers are likely to churn?
- What customer segments generate the most support tickets?
- What is the complete history of interactions with a customer?

---

## Architecture

```
                    Source Systems
                    CRM | Orders | Marketing | Support | Communications
                              |
                          Airflow
                     (DAG Orchestration)
                              |
                       Bronze Layer
                     (MinIO / S3 - Raw Parquet)
                              |
                   Spark (Scala) Transformations
                   Bronze -> Silver (Cleaned, Deduplicated)
                              |
                   Spark (Scala) Transformations
                   Silver -> Gold (Business Aggregates)
                              |
                    PostgreSQL Warehouse
                              |
                     AI Enrichment Layer
               Chunking | Embeddings | Classification | Sentiment
                              |
                            Qdrant
                    (Vector Storage)
                              |
                          LangGraph
            Customer Agent | Marketing Agent | Support Agent
                              |
                         Streamlit UI
```

### Medallion Lakehouse Paths

```
bronze/crm/
bronze/orders/
bronze/marketing/
bronze/support/
bronze/communications/

silver/customers/
silver/orders/
silver/marketing/
silver/support/

silver_ai/communications_clean/
silver_ai/chunks/
silver_ai/sentiment/
silver_ai/topics/
silver_ai/entities/

gold/customer_360/
gold/customer_ltv/
gold/marketing_analytics/
gold/support_analytics/
```

---

## Technology Stack

| Layer                | Technology                                      | Language   |
|----------------------|-------------------------------------------------|------------|
| Orchestration        | Apache Airflow 2.8.1 (CeleryExecutor)           | Python     |
| Ingestion            | Polars, PyArrow, boto3                          | Python     |
| Object Storage       | MinIO (S3-compatible)                           | N/A        |
| Transformations      | Apache Spark 3.5.3                              | Scala 2.13 |
| Build Tool           | sbt 2.0.0, sbt-assembly                        | Scala      |
| Data Format          | Parquet (Snappy compressed)                     | N/A        |
| Containerization     | Docker Compose                                  | N/A        |
| Vector Store         | Qdrant (planned)                                | N/A        |
| AI Framework         | LangGraph (planned)                             | Python     |
| UI                   | Streamlit (planned)                             | Python     |
| Warehouse            | PostgreSQL                                      | SQL        |

---

## Datasets

| Dataset                  | Records    | Format  | Source             |
|--------------------------|------------|---------|--------------------|
| CRM Customers            | 100,000    | JSONL   | Customer profiles  |
| Orders                   | 1,000,000  | CSV     | Sales transactions |
| Marketing Events         | 3,000,000  | CSV     | Campaign tracking  |
| Support Tickets          | 300,000    | JSONL   | Support operations |
| Customer Communications  | 767,000    | CSV     | Multi-channel comms |

Time period: January 2022 — present. Geographic distribution across US, Canada, Mexico, Colombia, Brazil, and Chile.
Customer segments: New, Active, Loyal, VIP, At Risk, and Churned.

---

## Project Structure

```
veector/
├── sources/                    # Raw synthetic data files (CSV, JSONL)
├── ingestion/                  # Python ingestion layer (Polars -> Parquet -> S3)
│   ├── job.py                  # Core IngestJob class
│   ├── sources.py              # Source configuration registry
│   └── {crm,orders,...}/       # Source-specific ingest jobs
├── transformations/            # Scala Spark transformations (Bronze -> Silver)
│   ├── build.sbt               # sbt build definition
│   ├── src/main/scala/
│   │   ├── shared/             # Shared Spark utilities (factory, reader, writer)
│   │   └── bronze_to_silver/   # Per-source transform jobs
│   └── artifacts/              # Built JARs
├── infrastructure/             # Docker Compose + configs
│   ├── airflow/                # Airflow stack (PostgreSQL, Redis, Webserver, Scheduler, Worker)
│   │   └── dags/               # DAG definitions (bronze ingestion, silver transforms)
│   ├── s3/                     # MinIO object storage
│   └── spark/                  # Spark cluster (master + worker)
├── lakehouse/                  # Lakehouse data mount
├── warehouse/                  # PostgreSQL warehouse (placeholder)
├── analytics/                  # Dashboards & BI (placeholder)
├── ai/                         # AI enrichment layer (placeholder)
├── scripts/                    # Utility scripts (MinIO setup)
├── tests/                      # Test suite (placeholder)
├── docs/                       # Project documentation
├── Dockerfile                  # Airflow + Spark image
├── Makefile                    # Service lifecycle management
└── requirements.txt            # Python dependencies
```

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Python 3.14+
- Java 17+ (for local Spark development)
- sbt 2.0.0 (for Scala builds)

### Environment Setup

```bash
cp .env.example .env
# Edit .env as needed (MinIO credentials, bucket name, etc.)
```

### Start Infrastructure

```bash
make up
```

This starts Airflow (CeleryExecutor), MinIO (S3), and Spark (master + worker) via Docker Compose.

### Run Ingestion

The Bronze ingestion DAGs are triggered automatically via Airflow on a daily schedule. To trigger manually:

```bash
# Via Airflow UI at http://localhost:8080
# Or via CLI:
docker compose -f infrastructure/airflow/compose.yaml run --rm airflow-cli dags trigger ingest_bronze_crm
```

Available DAGs:
- `ingest_bronze_crm`
- `ingest_bronze_orders`
- `ingest_bronze_marketing_events`
- `ingest_bronze_support_tickets`
- `ingest_bronze_customer_communications`

### Run Transformations (Bronze -> Silver)

```bash
# Build Spark assembly JAR
cd transformations && sbt assembly

# Trigger silver DAGs via Airflow
docker compose -f infrastructure/airflow/compose.yaml run --rm airflow-cli dags trigger crm_bronze_to_silver
```

### Stop Everything

```bash
make down
```

---

## Pipeline Lifecycle

1. **Source Data** — Raw CSV/JSONL files reside in `sources/`
2. **Bronze Ingestion** (Python/Polars) — Reads source data, converts to Snappy-compressed Parquet, uploads to MinIO partitioned by `year/month/day`
3. **Silver Transformation** (Scala/Spark) — Reads Bronze Parquet from MinIO, applies deduplication and cleansing, writes Silver Parquet back to MinIO
4. **Gold Aggregation** (planned) — Business-level aggregations (Customer 360, LTV, marketing analytics, support analytics)
5. **Warehouse** (planned) — Load into PostgreSQL for BI tooling
6. **AI Enrichment** (planned) — Chunking, embeddings, sentiment analysis, topic classification
7. **Vector Store + Agents** (planned) — Qdrant for vector search, LangGraph agents for customer/marketing/support insights
8. **UI** (planned) — Streamlit dashboards and customer 360 interface

---

## Development

### Adding a New Source

1. Add raw data file to `sources/`
2. Define an `IngestionConfig` in `ingestion/sources.py`
3. Create an ingest job class in `ingestion/{source_name}/ingest.py`
4. Add a Bronze DAG in `infrastructure/airflow/dags/bronze/`
5. Add a Silver transformer in `transformations/src/main/scala/bronze_to_silver/{source_name}/`
6. Register the new `DataSource` enum in `shared/enums/`
7. Add a Silver DAG in `infrastructure/airflow/dags/silver/`

### Adding a New Transformation

1. Create a `Transformer.scala` with the transformation logic
2. Create a `Job.scala` with the Spark job entry point
3. Run `sbt assembly` to build the JAR
4. Create a Silver DAG that calls `spark-submit`

---

## Documentation

Detailed documentation is available in `docs/`:

- [Project Description](docs/001-project-description.md) — Business context and goals
- [Datasets Description](docs/002-datasets-description.md) — Schemas, relationships, generation rules
- [Project Architecture](docs/003-project-architecture.md) — Architecture diagram and lakehouse paths
