package com.veector
package shared.silver

import com.veector.shared.TestSpark
import com.veector.shared.enums.DataSource
import munit.FunSuite
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

class TransformerBuilderSuite extends FunSuite {

  private val schema = StructType(Seq(
    StructField("id",   IntegerType, nullable = true),
    StructField("name", StringType,  nullable = true)
  ))

  test("default builder starts unconfigured") {
    assertEquals(TransformerBuilder().dataSource, None)
  }

  test("withDataSource sets the source") {
    assertEquals(TransformerBuilder().withDataSource(DataSource.Orders).dataSource, Some(DataSource.Orders))
  }

  test("build throws when dataSource is missing") {
    intercept[IllegalStateException] {
      TransformerBuilder().build()
    }
  }

  test("transform throws when dataSource is missing") {
    intercept[IllegalStateException] {
      TransformerBuilder().transform(TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice"))))
    }
  }

  test("transform deduplicates, normalizes and enriches with the configured source") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq(1, "  Alice  "),
      Seq(1, "  Alice  "),
      Seq(2, "  BOB  ")
    ))

    val result = TransformerBuilder().withDataSource(DataSource.Orders).transform(df)

    assertEquals(result.count(), 2L)
    assertEquals(result.select("name").collect().map(_.getString(0)).toSet, Set("alice", "bob"))
    assertEquals(result.select("source_system").distinct().collect().head.getString(0), DataSource.Orders.toString)
    assert(result.columns.contains("processing_timestamp"))
    assert(result.columns.contains("year"))
    assert(result.columns.contains("month"))
    assert(result.columns.contains("day"))
  }

  test("transform tags rows with the source that was configured, not a hardcoded one") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice")))

    val crm = TransformerBuilder().withDataSource(DataSource.CRM).transform(df)
    val tickets = TransformerBuilder().withDataSource(DataSource.SupportTickets).transform(df)

    assertEquals(crm.select("source_system").collect().head.getString(0), DataSource.CRM.toString)
    assertEquals(tickets.select("source_system").collect().head.getString(0), DataSource.SupportTickets.toString)
  }
}
