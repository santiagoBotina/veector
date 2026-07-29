package com.veector
package shared.silver

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{BooleanType, DateType, StringType, TimestampType}

object Normalizer {
  private val datePattern: String = "yyyy-MM-dd"
  private val timestampPattern: String = "yyyy-MM-dd HH:mm:ss"

  def applyTo(df: DataFrame): DataFrame = {
    val trimmedDF = trimStrings(df)

    var finalDf = trimmedDF

    df.schema.fields.foreach { field =>
      field.dataType match {

        case DateType =>
          finalDf = finalDf.withColumn(
            field.name,
            to_date(col(field.name), datePattern)
          )

        case TimestampType =>
          finalDf = finalDf.withColumn(
            field.name,
            to_timestamp(col(field.name), timestampPattern)
          )

        case BooleanType =>
          finalDf = finalDf.withColumn(
            field.name,
            caseWhenBoolean(field.name)
          )

        case _ => // Leave other types untouched
      }
    }

    finalDf
  }

  private def trimStrings(df: DataFrame): DataFrame = {
    // Get all string fields columns names
    val stringColumns = df.schema.fields
      .filter(_.dataType == StringType)
      .map(_.name)

    val stringTransforms = stringColumns.map { colName =>
      colName -> lower(trim(col(colName)))
    }.toMap

    // Apply string transformations to df
    df.withColumns(stringTransforms)
  }

  private def caseWhenBoolean(colName: String) = {
    when(lower(trim(col(colName))).isin("true", "1", "yes", "y", "t"), true)
      .when(lower(trim(col(colName))).isin("false", "0", "no", "n", "f"), false)
      .otherwise(null)
      .cast(BooleanType)
  }
}
