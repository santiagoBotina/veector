package com.veector
package shared.bronze

import com.veector.shared.TestSpark
import com.veector.shared.enums.DataSource
import munit.FunSuite
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

import java.io.File
import java.nio.file.Files

class WriterSuite extends FunSuite {

  private val schema = StructType(Seq(
    StructField("id",   IntegerType, nullable = true),
    StructField("name", StringType,  nullable = true)
  ))

  test("write persists the DataFrame as parquet") {
    val tmp = Files.createTempDirectory("veector-writer").toFile
    val path = new File(tmp, "out").getAbsolutePath
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice"), Seq(2, "bob")))

    Writer.write(df, path)

    val readBack = TestSpark.spark.read.parquet(path)
    assertEquals(readBack.count(), 2L)
  }

  test("toSilver writes to the source-specific subpath under the given base path") {
    val tmp = Files.createTempDirectory("veector-writer").toFile
    val base = new File(tmp, "silver")
    val df = TestSpark.dataFrameFromRows(schema, Seq(Seq(1, "alice")))

    Writer.toSilver(df, DataSource.Orders, base.getAbsolutePath)

    val ordersPath = new File(base, "orders")
    assert(ordersPath.exists())
    assertEquals(TestSpark.spark.read.parquet(ordersPath.getAbsolutePath).count(), 1L)
  }
}
