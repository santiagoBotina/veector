# 002 - Synthetic Data Generation Context

---

# Data Generation Requirements

## Global Requirements

Generate realistic synthetic data following these rules:

### Volume

Generate approximately:

| Dataset | Records |
|----------|----------|
| CRM Customers | 100,000 |
| Orders | 1,000,000 |
| Marketing Events | 3,000,000 |
| Support Tickets | 300,000 |

### Time Period

Generate data covering:

```text
January 2022 - Current Date
```

### Geographic Distribution

Customers should be distributed across:

- United States
- Canada
- Mexico
- Colombia
- Brazil
- Chile

### Customer Segments

Generate customers across:

- New Customer
- Active Customer
- Loyal Customer
- VIP Customer
- At Risk Customer
- Churned Customer

### Customer Behavior

The generated data should reflect realistic business behavior:

- VIP customers place more orders
- VIP customers spend more money
- Churned customers have no recent activity
- Active customers engage more with campaigns
- Customers with many orders tend to have more support interactions
- Support interactions increase after large purchases
- Marketing engagement should influence purchase probability

---

# Dataset 1: CRM Customers

## Description

Represents the source of truth for customer profile information.

One record per customer.

## Primary Key

```text
customer_id
```

## Data Model

| Field | Type | Description |
|---------|---------|---------|
| customer_id | UUID | Unique customer identifier |
| first_name | STRING | Customer first name |
| last_name | STRING | Customer last name |
| email | STRING | Primary email |
| phone_number | STRING | Phone number |
| birth_date | DATE | Date of birth |
| gender | STRING | Male, Female, Non-Binary, Prefer Not To Say |
| country | STRING | Country |
| city | STRING | City |
| postal_code | STRING | Postal code |
| registration_date | TIMESTAMP | Account creation date |
| customer_segment | STRING | Segment classification |
| preferred_language | STRING | Preferred language |
| preferred_channel | STRING | Email, SMS, Phone, WhatsApp |
| loyalty_tier | STRING | Bronze, Silver, Gold, Platinum |
| account_status | STRING | Active, Inactive, Suspended |
| marketing_opt_in | BOOLEAN | Consent for marketing |
| last_login_date | TIMESTAMP | Last platform login |
| acquisition_channel | STRING | Acquisition source |
| customer_risk_score | DECIMAL | Predicted churn score |

---

# Dataset 2: Orders

## Description

Represents all completed and cancelled purchases.

Multiple orders per customer.

## Primary Key

```text
order_id
```

## Foreign Key

```text
customer_id
```

References CRM Customers.

## Data Model

| Field | Type | Description |
|---------|---------|---------|
| order_id | UUID | Order identifier |
| customer_id | UUID | Customer identifier |
| order_date | TIMESTAMP | Order creation date |
| order_status | STRING | Completed, Cancelled, Refunded |
| order_channel | STRING | Web, Mobile App |
| payment_method | STRING | Credit Card, Debit Card, PayPal, Bank Transfer |
| shipping_country | STRING | Shipping country |
| shipping_city | STRING | Shipping city |
| product_category | STRING | Product category |
| product_sku | STRING | Product SKU |
| quantity | INTEGER | Quantity purchased |
| unit_price | DECIMAL | Unit price |
| discount_amount | DECIMAL | Discount applied |
| tax_amount | DECIMAL | Tax amount |
| shipping_cost | DECIMAL | Shipping cost |
| total_order_value | DECIMAL | Final order amount |
| promotion_code | STRING | Promotion code used |
| fulfillment_days | INTEGER | Days to deliver |
| order_profit_margin | DECIMAL | Margin percentage |
| customer_rating | INTEGER | Post-purchase rating |

---

# Dataset 3: Marketing Events

## Description

Tracks all customer interactions with marketing campaigns.

Many events per customer.

## Primary Key

```text
event_id
```

## Foreign Key

```text
customer_id
```

References CRM Customers.

## Data Model

| Field | Type | Description |
|---------|---------|---------|
| event_id | UUID | Event identifier |
| customer_id | UUID | Customer identifier |
| campaign_id | UUID | Campaign identifier |
| campaign_name | STRING | Campaign name |
| campaign_type | STRING | Email, SMS, Push, Social |
| event_type | STRING | Sent, Opened, Clicked, Converted |
| event_timestamp | TIMESTAMP | Event timestamp |
| marketing_channel | STRING | Channel used |
| device_type | STRING | Mobile, Desktop, Tablet |
| operating_system | STRING | OS used |
| browser | STRING | Browser used |
| referral_source | STRING | Traffic source |
| session_duration_seconds | INTEGER | Session duration |
| page_views | INTEGER | Pages viewed |
| conversion_flag | BOOLEAN | Conversion indicator |
| conversion_order_id | UUID | Related order |
| campaign_cost | DECIMAL | Campaign cost attribution |
| revenue_generated | DECIMAL | Revenue generated |
| customer_segment_snapshot | STRING | Segment at event time |
| engagement_score | DECIMAL | Calculated engagement score |

---

# Dataset 4: Support Tickets

## Description

Represents customer support interactions.

Multiple tickets per customer.

## Primary Key

```text
ticket_id
```

## Foreign Key

```text
customer_id
```

References CRM Customers.

## Data Model

| Field | Type | Description |
|---------|---------|---------|
| ticket_id | UUID | Ticket identifier |
| customer_id | UUID | Customer identifier |
| ticket_created_at | TIMESTAMP | Ticket creation time |
| ticket_closed_at | TIMESTAMP | Ticket resolution time |
| ticket_status | STRING | Open, Pending, Resolved, Escalated |
| support_channel | STRING | Email, Phone, Chat, WhatsApp |
| issue_category | STRING | Delivery, Billing, Product, Refund |
| issue_subcategory | STRING | Detailed issue classification |
| priority_level | STRING | Low, Medium, High, Critical |
| assigned_team | STRING | Team responsible |
| assigned_agent_id | STRING | Agent identifier |
| first_response_minutes | INTEGER | Time to first response |
| resolution_time_hours | DECIMAL | Resolution time |
| escalation_flag | BOOLEAN | Escalated ticket |
| related_order_id | UUID | Related order |
| customer_satisfaction_score | INTEGER | CSAT score |
| ticket_sentiment | STRING | Positive, Neutral, Negative |
| ticket_language | STRING | Language used |
| reopened_flag | BOOLEAN | Reopened ticket |
| support_cost_usd | DECIMAL | Estimated support cost |

---

# Business Rules

## CRM → Orders

Relationship:

```text
1 Customer -> N Orders
```

Rules:

- 20% of customers should never purchase.
- VIP customers should generate the highest order volume.
- Loyal customers should have recurring purchases.
- Churned customers should have no purchases in the last 12 months.

---

## CRM → Marketing

Relationship:

```text
1 Customer -> N Marketing Events
```

Rules:

- Customers with marketing_opt_in = false should have no marketing events.
- Active customers should receive more campaigns.
- Conversion events should be linked to orders.

---

## CRM → Support

Relationship:

```text
1 Customer -> N Support Tickets
```

Rules:

- Support volume should correlate with order volume.
- High-value customers receive faster support.
- Critical tickets should be less frequent.

---

# Gold Layer Business Outputs

The generated datasets must support creation of:

## Customer 360

Single customer profile including:

- Customer demographics
- Lifetime value
- Total orders
- Total spend
- Marketing engagement
- Support history
- Churn indicators

---

## Customer Lifetime Value Model

Metrics:

- Total Revenue
- Average Order Value
- Purchase Frequency
- Customer Age
- Lifetime Value

---

## Marketing Analytics

Metrics:

- Campaign ROI
- Conversion Rate
- Open Rate
- Click Through Rate
- Revenue Attribution

---

## Support Analytics

Metrics:

- Ticket Volume
- Resolution Time
- Escalation Rate
- Customer Satisfaction
- Cost Per Ticket

---

# Expected Data Engineering Outcomes

The generated data should allow implementation of:

- Bronze Layer (Raw Data)
- Silver Layer (Cleaned Data)
- Gold Layer (Business Aggregates)
- Data Quality Checks
- Slowly Changing Dimensions
- Fact Tables
- Dimension Tables
- Customer Identity Resolution
- Customer 360 Analytics
- Executive Dashboards

The synthetic data should be realistic enough that an experienced Data Engineer, Data Analyst, or Analytics Engineer would recognize the datasets as representative of a real production environment.