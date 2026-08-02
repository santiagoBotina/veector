package com.veector
package shared

import org.apache.spark.sql.types.{BooleanType, DateType, DoubleType, IntegerType, LongType, StringType, StructType, TimestampType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/** Shared local SparkSession and small helpers used across the test suites. */
object TestSpark {
  lazy val spark: SparkSession = {
    // Production passes the master via spark-submit; tests rely on a local session. SparkConf
    // picks up these `spark.*` system properties when the builder calls getOrCreate().
    System.setProperty("spark.master", "local[*]")
    System.setProperty("spark.ui.enabled", "false")
    System.setProperty("spark.ui.showConsoleProgress", "false")
    System.setProperty("spark.sql.shuffle.partitions", "2")

    val session = SparkFactory.create("test-app")
    session.sparkContext.setLogLevel("WARN")
    session
  }

  def dataFrameFromRows(schema: StructType, rows: Seq[Seq[Any]]): DataFrame = {
    val rdd = spark.sparkContext.parallelize(rows.map(Row.fromSeq))
    spark.createDataFrame(rdd, schema)
  }

  /** One representative value per data type, aligned with the order of the schema fields. */
  def defaultValuesFor(schema: StructType): Seq[Any] = schema.fields.map { field =>
    field.dataType match {
      case StringType    => "value"
      case IntegerType   => 1
      case LongType      => 1L
      case DoubleType    => 1.0
      case BooleanType   => true
      case DateType      => java.sql.Date.valueOf("2024-01-01")
      case TimestampType => java.sql.Timestamp.valueOf("2024-01-01 10:00:00")
      case _             => null
    }
  }
}
