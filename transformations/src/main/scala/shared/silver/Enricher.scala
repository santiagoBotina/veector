package com.veector
package shared.silver

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import com.veector.shared.enums.DataSource

object Enricher {
  def applyTo(df: DataFrame, source: DataSource): DataFrame = {

    val currentBatchTimestamp = current_timestamp()

    df
      .withColumn("processing_timestamp", currentBatchTimestamp)
      .withColumn("source_system", lit(source.toString)) // Converts enum to plain String representation

      .withColumn("year", year(currentBatchTimestamp))
      .withColumn("month", format_string("%02d", month(currentBatchTimestamp))) // Zero-pads month (e.g., "01", "11")
      .withColumn("day", format_string("%02d", dayofmonth(currentBatchTimestamp))) // Zero-pads day (e.g., "05", "25")
  }
}
