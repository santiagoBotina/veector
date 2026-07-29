package com.veector
package bronze_to_silver.orders

import com.veector.shared.{Logger, Reader, SparkFactory}
import shared.enums.DataSource
import com.veector.shared.bronze.Writer
import org.apache.spark.sql.SparkSession
import scala.util.control.NonFatal

object Job {

  def main(args: Array[String]): Unit = {
    Logger.log(s"Starting JOB execution for source: ${DataSource.Orders}")
    var spark: SparkSession = null

    try {
      spark = SparkFactory.create("orders-bronze-to-silver")

      Logger.log(s"Attempting to read from bronze layer for source: ${DataSource.Orders}")
      val bronzeDf = Reader.fromBronze(spark, DataSource.Orders)
      Logger.log(s"Successful read from bronze layer to source: ${DataSource.Orders}")

      Logger.log(s"Starting data transformation/normalization for source: ${DataSource.Orders}")
      val silverDf = Transformer.transform(bronzeDf)
      Logger.log(s"Successful transform task execution for source: ${DataSource.Orders}")

      Logger.log(s"Writing records to silver layer for source: ${DataSource.Orders}")
      Writer.toSilver(silverDf, DataSource.Orders)
      Logger.log(s"Successful write for cleaned, normalized and enriched source: ${DataSource.Orders}")

    } catch {
      case NonFatal(e) =>
        Logger.error(s"CRITICAL CRASH: Bronze-to-Silver Job failed for source: ${DataSource.Orders}", e)
        sys.exit(1)

    } finally {
      if (spark != null) {
        Logger.log("Stopping active Spark Session context...")
        spark.stop()
      }
      Logger.log(s"JOB execution phase concluded for source: ${DataSource.Orders}")
    }
  }
}
