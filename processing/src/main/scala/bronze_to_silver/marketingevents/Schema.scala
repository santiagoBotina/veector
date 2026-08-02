package com.veector
package bronze_to_silver.marketingevents

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType, TimestampType}

object Schema {
  val MarketingEventsSchema: StructType = StructType(Seq(
    StructField("event_id",                  IntegerType,   nullable = false),
    StructField("customer_id",               StringType,    nullable = false),
    StructField("campaign_id",               IntegerType,   nullable = true),
    StructField("campaign_name",             StringType,    nullable = true),
    StructField("campaign_type",             StringType,    nullable = true),
    StructField("event_type",                StringType,    nullable = true),
    StructField("event_timestamp",           TimestampType, nullable = true),
    StructField("device_type",               StringType,    nullable = true),
    StructField("operating_system",          StringType,    nullable = true),
    StructField("browser",                   StringType,    nullable = true),
    StructField("referral_source",           StringType,    nullable = true),
    StructField("session_duration_seconds",  IntegerType,   nullable = true),
    StructField("page_views",                IntegerType,   nullable = true),
    StructField("conversion_flag",           IntegerType,   nullable = true),
    StructField("conversion_order_id",       StringType,    nullable = true),
    StructField("campaign_cost",             DoubleType,    nullable = true),
    StructField("revenue_generated",         DoubleType,    nullable = true),
    StructField("customer_segment_snapshot", StringType,    nullable = true),
    StructField("engagement_score",          DoubleType,    nullable = true)
  ))
}
