package com.veector
package shared

import shared.enums.DataSource

import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {
  val bronzeBasePath: String = "s3a://veector-lakehouse/bronze";

  def fromBronze(spark: SparkSession, dataSource: DataSource): DataFrame = {
    dataSource match
    {
      case DataSource.CRM => parquet(spark, s"$bronzeBasePath/crm")
      case DataSource.CustomerCommunications => parquet(spark, s"$bronzeBasePath/communications")
      case DataSource.MarketingEvents => parquet(spark, s"$bronzeBasePath/marketing")
      case DataSource.Orders => parquet(spark, s"$bronzeBasePath/orders")
      case DataSource.SupportTickets => parquet(spark, s"$bronzeBasePath/support")
    }
  }

  def parquet(spark: SparkSession, filePath: String): DataFrame = {
    spark.read
      .parquet(filePath)
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
