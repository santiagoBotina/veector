package com.veector
package bronze_to_silver

import com.veector.shared.TestSpark
import com.veector.shared.enums.DataSource
import munit.FunSuite
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/** Shared behavior checks for each per-source [[Transformer]]. */
abstract class TransformerPipelineSuite(source: DataSource, transform: DataFrame => DataFrame) extends FunSuite {

  private val schema = StructType(Seq(
    StructField("id",   IntegerType, nullable = true),
    StructField("name", StringType,  nullable = true)
  ))

  test("transform deduplicates rows") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq(1, "  Alice  "),
      Seq(1, "  Alice  "),
      Seq(2, "  BOB  ")
    ))

    assertEquals(transform(df).count(), 2L)
  }

  test("transform normalizes string columns") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq(1, "  Alice  "),
      Seq(2, "  BOB  ")
    ))

    val names = transform(df).select("name").collect().map(_.getString(0)).toSet
    assertEquals(names, Set("alice", "bob"))
  }

  test("transform enriches with the expected source system") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice")))

    val result = transform(df)

    assertEquals(result.select("source_system").distinct().collect().head.getString(0), source.toString)
    assert(result.columns.contains("processing_timestamp"))
    assert(result.columns.contains("year"))
    assert(result.columns.contains("month"))
    assert(result.columns.contains("day"))
  }
}
