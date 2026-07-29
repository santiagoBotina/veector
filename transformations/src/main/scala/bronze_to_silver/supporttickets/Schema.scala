package com.veector
package bronze_to_silver.supporttickets

import org.apache.spark.sql.types.{BooleanType, DoubleType, IntegerType, StringType, StructField, StructType, TimestampType}

object Schema {
  val SupportTicketsSchema: StructType = StructType(Seq(
    StructField("ticket_id",                  StringType,    nullable = false),
    StructField("customer_id",                StringType,    nullable = false),
    StructField("ticket_created_at",          TimestampType, nullable = true),
    StructField("ticket_closed_at",           TimestampType, nullable = true),
    StructField("ticket_status",              StringType,    nullable = true),
    StructField("support_channel",            StringType,    nullable = true),
    StructField("issue_category",             StringType,    nullable = true),
    StructField("issue_subcategory",          StringType,    nullable = true),
    StructField("priority_level",             StringType,    nullable = true),
    StructField("assigned_team",              StringType,    nullable = true),
    StructField("assigned_agent_id",          StringType,    nullable = true),
    StructField("first_response_minutes",     IntegerType,   nullable = true),
    StructField("resolution_time_hours",      DoubleType,    nullable = true),
    StructField("escalation_flag",            BooleanType,   nullable = true),
    StructField("related_order_id",           StringType,    nullable = true),
    StructField("customer_satisfaction_score",IntegerType,   nullable = true),
    StructField("ticket_sentiment",           StringType,    nullable = true),
    StructField("ticket_language",            StringType,    nullable = true),
    StructField("reopened_flag",              BooleanType,   nullable = true),
    StructField("support_cost_usd",           DoubleType,    nullable = true)
  ))
}
