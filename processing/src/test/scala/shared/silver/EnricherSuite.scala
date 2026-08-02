package com.veector
package shared.silver

import com.veector.shared.TestSpark
import com.veector.shared.enums.DataSource
import munit.FunSuite
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

class EnricherSuite extends FunSuite {

  private val schema = StructType(Seq(
    StructField("id",   IntegerType, nullable = true),
    StructField("name", StringType,  nullable = true)
  ))

  test("adds the processing timestamp column") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice")))
    val result = Enricher.applyTo(df, DataSource.Orders)

    assert(result.columns.contains("processing_timestamp"))
    assert(!result.select("processing_timestamp").collect().head.isNullAt(0))
  }

  test("tags every row with the configured source system") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice"), Seq(2, "bob")))
    val result = Enricher.applyTo(df, DataSource.SupportTickets)

    val sources = result.select("source_system").collect().map(_.getString(0)).toSet
    assertEquals(sources, Set(DataSource.SupportTickets.toString))
  }

  test("adds zero-padded year, month and day partitioning columns") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice")))
    val result = Enricher.applyTo(df, DataSource.CRM)

    assert(result.columns.contains("year"))
    assert(result.columns.contains("month"))
    assert(result.columns.contains("day"))

    val row = result.select("year", "month", "day").collect().head
    assertEquals(row.getString(1).length, 2)
    assertEquals(row.getString(2).length, 2)
  }

  test("preserves the input columns and row count") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice"), Seq(2, "bob")))
    val result = Enricher.applyTo(df, DataSource.MarketingEvents)

    assertEquals(result.count(), 2L)
    assert(result.columns.contains("id"))
    assert(result.columns.contains("name"))
    assertEquals(result.select("id").collect().map(_.getInt(0)).toSet, Set(1, 2))
  }
}
