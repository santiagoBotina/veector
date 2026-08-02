package com.veector
package bronze_to_silver.crm

import org.apache.spark.sql.types._

object Schema {
  val CRMSchema: StructType = StructType(Seq(
    StructField("customer_id",         StringType,    nullable = false),
    StructField("first_name",         StringType,    nullable = true),
    StructField("last_name",          StringType,    nullable = true),
    StructField("email",              StringType,    nullable = true),
    StructField("phone_number",       StringType,    nullable = true),
    StructField("birth_date",         DateType,      nullable = true),
    StructField("gender",             StringType,    nullable = true),
    StructField("country",            StringType,    nullable = true),
    StructField("city",               StringType,    nullable = true),
    StructField("postal_code",        StringType,    nullable = true),
    StructField("registration_date",  TimestampType, nullable = true),
    StructField("customer_segment",   StringType,    nullable = true),
    StructField("preferred_language", StringType,    nullable = true),
    StructField("preferred_channel",  StringType,    nullable = true),
    StructField("loyalty_tier",       StringType,    nullable = true),
    StructField("account_status",     StringType,    nullable = true),
    StructField("marketing_opt_in",   BooleanType,   nullable = true),
    StructField("last_login_date",    TimestampType, nullable = true),
    StructField("acquisition_channel",StringType,    nullable = true),
    StructField("customer_risk_score",DoubleType,    nullable = true)
  ))
}
