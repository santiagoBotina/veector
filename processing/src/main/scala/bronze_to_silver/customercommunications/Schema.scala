package com.veector
package bronze_to_silver.customercommunications

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType, TimestampType}

object Schema {
  val CustomerCommunicationsSchema: StructType = StructType(Seq(
    StructField("communication_id",           StringType,    nullable = false),
    StructField("customer_id",                StringType,    nullable = false),
    StructField("order_id",                   StringType,    nullable = true),
    StructField("ticket_id",                  StringType,    nullable = true),
    StructField("interaction_timestamp",      TimestampType, nullable = true),
    StructField("channel",                    StringType,    nullable = true),
    StructField("direction",                  StringType,    nullable = true),
    StructField("language",                   StringType,    nullable = true),
    StructField("subject",                    StringType,    nullable = true),
    StructField("message_text",               StringType,    nullable = true),
    StructField("communication_type",         StringType,    nullable = true),
    StructField("customer_mood",              StringType,    nullable = true),
    StructField("urgency_level",              StringType,    nullable = true),
    StructField("communication_status",       StringType,    nullable = true),
    StructField("product_category",           StringType,    nullable = true),
    StructField("product_sku",                StringType,    nullable = true),
    StructField("detected_topic",             StringType,    nullable = true),
    StructField("contains_complaint",         IntegerType,   nullable = true),
    StructField("contains_refund_request",    IntegerType,   nullable = true),
    StructField("contains_purchase_intent",   IntegerType,   nullable = true),
    StructField("contains_escalation_request",IntegerType,   nullable = true),
    StructField("sentiment_label",            StringType,    nullable = true),
    StructField("sentiment_score",            DoubleType,    nullable = true),
    StructField("generated_source",           StringType,    nullable = true),
    StructField("response_time_minutes",      IntegerType,   nullable = true),
    StructField("customer_segment_snapshot",  StringType,    nullable = true)
  ))
}
