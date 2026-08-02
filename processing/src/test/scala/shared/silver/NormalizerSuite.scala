package com.veector
package shared.silver

import com.veector.shared.TestSpark
import munit.FunSuite
import org.apache.spark.sql.types._

class NormalizerSuite extends FunSuite {

  private val schema = StructType(Seq(
    StructField("name",       StringType,    nullable = true),
    StructField("birth_date", DateType,      nullable = true),
    StructField("created_at", TimestampType, nullable = true),
    StructField("is_active",  BooleanType,   nullable = true),
    StructField("quantity",   IntegerType,   nullable = true),
    StructField("price",      DoubleType,    nullable = true)
  ))

  test("trims and lowercases string columns") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq("  Alice  ", null, null, null, null, null)))
    val result = Normalizer.applyTo(df)

    assertEquals(result.select("name").collect().head.getString(0), "alice")
  }

  test("normalizes DateType columns with the configured pattern") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(null, java.sql.Date.valueOf("2024-01-05"), null, null, null, null)))
    val result = Normalizer.applyTo(df)

    assertEquals(result.select("birth_date").collect().head.get(0), java.sql.Date.valueOf("2024-01-05"))
  }

  test("normalizes TimestampType columns with the configured pattern") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(null, null, java.sql.Timestamp.valueOf("2024-01-05 10:30:00"), null, null, null)))
    val result = Normalizer.applyTo(df)

    assertEquals(result.select("created_at").collect().head.get(0), java.sql.Timestamp.valueOf("2024-01-05 10:30:00"))
  }

  test("keeps true boolean values as true") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq(null, null, null, true, null, null),
      Seq(null, null, null, true, null, null)
    ))
    val result = Normalizer.applyTo(df)

    val booleans = result.select("is_active").collect().map(_.getBoolean(0)).toSet
    assertEquals(booleans, Set(true))
  }

  test("keeps false boolean values as false") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq(null, null, null, false, null, null),
      Seq(null, null, null, false, null, null)
    ))
    val result = Normalizer.applyTo(df)

    val booleans = result.select("is_active").collect().map(_.getBoolean(0)).toSet
    assertEquals(booleans, Set(false))
  }

  test("keeps null boolean values as null") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(null, null, null, null, null, null)))
    val result = Normalizer.applyTo(df)

    assert(result.select("is_active").collect().head.isNullAt(0))
  }

  test("leaves non-string, non-date, non-timestamp, non-boolean columns untouched") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(null, null, null, null, 42, 19.99)))
    val result = Normalizer.applyTo(df)

    assertEquals(result.select("quantity").collect().head.getInt(0), 42)
    assertEquals(result.select("price").collect().head.getDouble(0), 19.99)
  }

  test("preserves row count and schema") {
    val df = TestSpark.dataFrameFromRows(schema, Seq(
      Seq("  Alice  ", java.sql.Date.valueOf("2024-01-01"), java.sql.Timestamp.valueOf("2024-01-01 10:00:00"), true, 1, 1.0),
      Seq("  Bob  ", java.sql.Date.valueOf("2024-02-02"), java.sql.Timestamp.valueOf("2024-02-02 10:00:00"), false, 2, 2.0)
    ))
    val result = Normalizer.applyTo(df)

    assertEquals(result.count(), 2L)
    assertEquals(result.columns.sorted.toSeq, schema.fieldNames.sorted.toSeq)
  }
}
