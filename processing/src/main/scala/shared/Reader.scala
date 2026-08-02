package com.veector
package shared

import bronze_to_silver.crm.{Schema => CrmSchemaObj}
import bronze_to_silver.customercommunications.{Schema => CommSchemaObj}
import bronze_to_silver.marketingevents.{Schema => MarketingSchemaObj}
import bronze_to_silver.orders.{Schema => OrdersSchemaObj}
import bronze_to_silver.supporttickets.{Schema => SupportSchemaObj}
import shared.enums.DataSource

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {
  private val bronzeBasePath: String = "s3a://veector-lakehouse/bronze";

  def fromBronze(spark: SparkSession, dataSource: DataSource): DataFrame = {
    Logger.log(s"Starting reading from bronze layer - datasource: $dataSource")

    val (path, schema) = dataSource match {
      case DataSource.CRM =>
        (s"$bronzeBasePath/crm", CrmSchemaObj.CRMSchema)
      case DataSource.CustomerCommunications =>
        (s"$bronzeBasePath/communications", CommSchemaObj.CustomerCommunicationsSchema)
      case DataSource.MarketingEvents =>
        (s"$bronzeBasePath/marketing", MarketingSchemaObj.MarketingEventsSchema)
      case DataSource.Orders =>
        (s"$bronzeBasePath/orders", OrdersSchemaObj.OrdersSchema)
      case DataSource.SupportTickets =>
        (s"$bronzeBasePath/support", SupportSchemaObj.SupportTicketsSchema)
    }

    readAndValidateParquet(spark, path, schema)
  }

  private def readAndValidateParquet(spark: SparkSession, filePath: String, targetSchema: StructType): DataFrame = {
    val rawDf = spark.read.parquet(filePath)

    val validationConditions = targetSchema.fields.map { field =>
      if (!field.nullable) {
        col(field.name).isNotNull
      } else {
        // Check if the col can be castable to target type without converting valid data to null
        col(field.name).cast(field.dataType).isNotNull.or(col(field.name).isNull)
      }
    }.reduce(_ and _)

    val verifiedDf = rawDf.filter(validationConditions)
    val corruptedDf = rawDf.filter(not(validationConditions))

    if (!corruptedDf.isEmpty) {
      corruptedDf.write
        .mode("append")
        .parquet(s"$filePath/dlq/${System.currentTimeMillis()}")
    }

    verifiedDf.select(
      targetSchema.fields.map(f => col(f.name).cast(f.dataType).as(f.name)): _*
    )
  }

  def jsonl(spark: SparkSession, filePath: String): DataFrame = {
    spark.read
      .option("multiline", "true")
      .json(filePath)
  }

  def csv(spark: SparkSession, filePath: String): DataFrame = {
    spark.read
      .option("header", "true")
      .csv(filePath)
  }

}
