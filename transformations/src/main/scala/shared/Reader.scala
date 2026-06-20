package com.veector
package shared

import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {

  def parquet(spark: SparkSession, filePath: String): DataFrame = {
    // TODO: check how to read multiple parquets at the same time
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
