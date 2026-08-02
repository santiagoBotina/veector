package com.veector
package bronze_to_silver.orders

import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType, TimestampType}

object Schema {
  val OrdersSchema: StructType = StructType(Seq(
    StructField("order_id",            StringType,    nullable = false),
    StructField("customer_id",         StringType,    nullable = false),
    StructField("order_date",          TimestampType, nullable = true),
    StructField("order_status",        StringType,    nullable = true),
    StructField("order_channel",       StringType,    nullable = true),
    StructField("payment_method",      StringType,    nullable = true),
    StructField("shipping_country",    StringType,    nullable = true),
    StructField("shipping_city",       StringType,    nullable = true),
    StructField("product_category",    StringType,    nullable = true),
    StructField("product_sku",         StringType,    nullable = true),
    StructField("quantity",            IntegerType,   nullable = true),
    StructField("unit_price",          DoubleType,    nullable = true),
    StructField("discount_amount",     DoubleType,    nullable = true),
    StructField("tax_amount",          DoubleType,    nullable = true),
    StructField("shipping_cost",       DoubleType,    nullable = true),
    StructField("total_order_value",   DoubleType,    nullable = true),
    StructField("promotion_code",      StringType,    nullable = true),
    StructField("fulfillment_days",    IntegerType,   nullable = true),
    StructField("order_profit_margin", DoubleType,    nullable = true),
    StructField("customer_rating",     IntegerType,   nullable = true)
  ))
}
