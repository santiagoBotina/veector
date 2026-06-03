# 001 - Project Description

---

## Overview

This project simulates a modern enterprise-grade Customer 360 Data Platform built using Data Engineering best practices and a Lakehouse architecture.

The goal is to consolidate customer information from multiple operational systems into a unified customer profile that can be used for analytics, reporting, marketing optimization, customer support insights, and business intelligence.

The project will implement:

- Data Lake architecture
- Medallion Architecture (Bronze, Silver, Gold)
- Batch ETL Pipelines
- Airflow DAG Orchestration
- Spark Transformations
- Data Quality Validation
- Customer Identity Resolution
- Data Warehouse Modeling
- Analytics Dashboards

The generated datasets should resemble realistic production data from a mid-sized e-commerce company operating in North America and Latin America.

---

# Business Context

The company is an online retailer selling consumer electronics, accessories, and smart home products.

The organization uses several disconnected systems:

| System | Purpose |
|----------|----------|
| CRM | Customer Management |
| Orders | Sales Transactions |
| Marketing | Campaign and Engagement Tracking |
| Support | Customer Service Operations |

Leadership wants to answer questions such as:

- What is the lifetime value of each customer?
- Which marketing campaigns drive the highest revenue?
- Which customers are likely to churn?
- What customer segments generate the most support tickets?
- What is the complete history of interactions with a customer?
