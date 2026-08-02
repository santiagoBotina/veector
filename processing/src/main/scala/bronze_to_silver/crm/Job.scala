package com.veector
package bronze_to_silver.crm

import shared.enums.DataSource
import com.veector.shared.bronze.Writer
import com.veector.shared.{Logger, Reader, SparkFactory}
import org.apache.spark.sql.SparkSession
import scala.util.control.NonFatal

object Job {

  def main(args: Array[String]): Unit = {
    Logger.log(s"Starting JOB execution for source: ${DataSource.CRM}")

    var spark: SparkSession = null

    try {
      spark = SparkFactory.create("crm-bronze-to-silver")

      Logger.log(s"Attempting to read from bronze layer for source: ${DataSource.CRM}")
      val bronzeDf = Reader.fromBronze(spark, DataSource.CRM)
      Logger.log(s"Successful read from bronze layer to source: ${DataSource.CRM}")

      Logger.log(s"Starting data transformation/normalization for source: ${DataSource.CRM}")
      val silverDf = Transformer.transform(bronzeDf)
      Logger.log(s"Successful transform task execution for source: ${DataSource.CRM}")

      Logger.log(s"Writing records to silver layer for source: ${DataSource.CRM}")
      Writer.toSilver(silverDf, DataSource.CRM)
      Logger.log(s"Successful write for cleaned, normalized and enriched source: ${DataSource.CRM}")

    } catch {
      case NonFatal(e) =>
        Logger.error(s"CRITICAL CRASH: Bronze-to-Silver Job failed for source: ${DataSource.CRM}", e)
        sys.exit(1) // Signaling Airflow that the job failed

    } finally {
      if (spark != null) {
        Logger.log("Stopping active Spark Session context...")
        spark.stop()
      }
      Logger.log(s"JOB execution phase concluded for source: ${DataSource.CRM}")
    }
  }
}
