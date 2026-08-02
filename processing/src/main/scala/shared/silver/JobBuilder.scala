package com.veector
package shared.silver

import com.veector.shared.bronze.Writer
import com.veector.shared.enums.DataSource
import com.veector.shared.{Logger, Reader, SparkFactory}
import org.apache.spark.sql.SparkSession

import scala.util.control.NonFatal

/** Immutable result of a [[JobBuilder]]: a fully configured bronze-to-silver job. */
case class BronzeToSilverJob(dataSource: DataSource, appName: String) {
  def run(): Unit = {
    Logger.log(s"Starting JOB execution for source: $dataSource")

    var spark: SparkSession = null

    try {
      spark = SparkFactory.create(appName)

      Logger.log(s"Attempting to read from bronze layer for source: $dataSource")
      val bronzeDf = Reader.fromBronze(spark, dataSource)
      Logger.log(s"Successful read from bronze layer to source: $dataSource")

      Logger.log(s"Starting data transformation/normalization for source: $dataSource")
      val silverDf = TransformerBuilder()
        .withDataSource(dataSource)
        .transform(bronzeDf)
      Logger.log(s"Successful transform task execution for source: $dataSource")

      Logger.log(s"Writing records to silver layer for source: $dataSource")
      Writer.toSilver(silverDf, dataSource)
      Logger.log(s"Successful write for cleaned, normalized and enriched source: $dataSource")

    } catch {
      case NonFatal(e) =>
        Logger.error(s"CRITICAL CRASH: Bronze-to-Silver Job failed for source: $dataSource", e)
        sys.exit(1) // Signaling Airflow that the job failed

    } finally {
      if (spark != null) {
        Logger.log("Stopping active Spark Session context...")
        spark.stop()
      }
      Logger.log(s"JOB execution phase concluded for source: $dataSource")
    }
  }
}

/**
 * Builder for the bronze-to-silver job orchestration. The only mandatory field is `dataSource`;
 * `appName` defaults to a kebab-case derivation of the source name.
 */
case class JobBuilder(
  dataSource: Option[DataSource] = None,
  appName: Option[String] = None
) {
  def withDataSource(dataSource: DataSource): JobBuilder = copy(dataSource = Some(dataSource))

  def withAppName(appName: String): JobBuilder = copy(appName = Some(appName))

  def build(): BronzeToSilverJob = {
    val source = dataSource.getOrElse(
      throw new IllegalStateException("dataSource is required to build a BronzeToSilverJob")
    )
    BronzeToSilverJob(source, appName.getOrElse(defaultAppName(source)))
  }

  def run(): Unit = build().run()

  private def defaultAppName(source: DataSource): String =
    s"${JobBuilder.kebabCase(source.toString)}-bronze-to-silver"
}

object JobBuilder {
  private[shared] def kebabCase(value: String): String =
    value
      .replaceAll("([a-z0-9])([A-Z])", "$1-$2")
      .replaceAll("([A-Z]+)([A-Z][a-z])", "$1-$2")
      .toLowerCase
}
