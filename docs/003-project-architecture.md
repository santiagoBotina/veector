# 003 - Project Architecture Overview

---

## Diagram

```
                     VEECTOR

+--------------------------------------------------+
|                Source Systems                    |
+--------------------------------------------------+

CRM
Orders
Marketing
Support
Communications

                ↓

+--------------------------------------------------+
|                   Airflow                         |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|                 Bronze Layer                      |
|                    MinIO                          |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|               Spark (Scala)                       |
|          Bronze -> Silver                         |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|               Spark (Scala)                       |
|          Silver -> Gold                           |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|             PostgreSQL Warehouse                  |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|              AI Enrichment Layer                  |
+--------------------------------------------------+

Chunking
Embeddings
Classification
Sentiment

                ↓

+--------------------------------------------------+
|                  Qdrant                           |
+--------------------------------------------------+

                ↓

+--------------------------------------------------+
|                LangGraph                          |
+--------------------------------------------------+

Customer Agent
Marketing Agent
Support Agent

                ↓

+--------------------------------------------------+
|                 Streamlit UI                      |
+--------------------------------------------------+
```

## Lakehouse Structure

```
"bronze/crm/"
"bronze/orders/"
"bronze/marketing/"
"bronze/support/"
"bronze/communications/"

"silver/customers/"
"silver/orders/"
"silver/marketing/"
"silver/support/"

"silver_ai/communications_clean/"
"silver_ai/chunks/"
"silver_ai/sentiment/"
"silver_ai/topics/"
"silver_ai/entities/"

"gold/customer_360/"
"gold/customer_ltv/"
"gold/marketing_analytics/"
"gold/support_analytics/"
```
