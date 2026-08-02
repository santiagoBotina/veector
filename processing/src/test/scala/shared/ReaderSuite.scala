package com.veector
package shared

import bronze_to_silver.crm.{Schema => CrmSchemaObj}
import com.veector.shared.enums.DataSource
import munit.FunSuite
import org.apache.spark.sql.types.StructType

import java.io.File
import java.nio.file.Files

class ReaderSuite extends FunSuite {

  test("csv reads a header csv into a DataFrame") {
    val tmp = Files.createTempDirectory("veector-reader").toFile
    val csvPath = new File(tmp, "data.csv")
    Files.write(csvPath.toPath, "id,name\n1,alice\n2,bob\n".getBytes)

    val df = Reader.csv(TestSpark.spark, csvPath.getAbsolutePath)

    assertEquals(df.count(), 2L)
    assertEquals(df.select("name").collect().map(_.getString(0)).toSet, Set("alice", "bob"))
  }

  test("jsonl reads json records into a DataFrame") {
    val tmp = Files.createTempDirectory("veector-reader").toFile
    val jsonPath = new File(tmp, "data.json")
    Files.write(jsonPath.toPath, "[\n{\"id\":1,\"name\":\"alice\"},\n{\"id\":2,\"name\":\"bob\"}\n]\n".getBytes)

    val df = Reader.jsonl(TestSpark.spark, jsonPath.getAbsolutePath)

    assertEquals(df.count(), 2L)
    assertEquals(df.select("id").collect().map(_.getLong(0)).toSet, Set(1L, 2L))
  }

  test("fromBronze reads and casts a valid bronze parquet for the given source") {
    val tmp = Files.createTempDirectory("veector-reader").toFile
    val bronzeBase = new File(tmp, "bronze")
    val bronze = TestSpark.dataFrameFromRows(CrmSchemaObj.CRMSchema, Seq(TestSpark.defaultValuesFor(CrmSchemaObj.CRMSchema)))
    bronze.write.mode("overwrite").parquet(new File(bronzeBase, "crm").getAbsolutePath)

    val df = Reader.fromBronze(TestSpark.spark, DataSource.CRM, bronzeBase.getAbsolutePath)

    assertEquals(df.count(), 1L)
    assertEquals(df.columns.sorted.toSeq, CrmSchemaObj.CRMSchema.fieldNames.sorted.toSeq)
  }

  test("fromBronze routes corrupt rows to a DLQ and keeps only the valid ones") {
    val tmp = Files.createTempDirectory("veector-reader").toFile
    val bronzeBase = new File(tmp, "bronze")

    // Spark refuses to encode a null into a non-nullable column, so the bronze fixture uses a
    // fully-nullable schema. Reader.fromBronze validates it against the real (non-nullable) schema.
    val nullableSchema = StructType(CrmSchemaObj.CRMSchema.fields.map(_.copy(nullable = true)))
    val valid = TestSpark.defaultValuesFor(nullableSchema)
    val corrupt = valid.updated(0, null) // customer_id is non-nullable in the target schema
    val bronze = TestSpark.dataFrameFromRows(nullableSchema, Seq(valid, corrupt))
    bronze.write.mode("overwrite").parquet(new File(bronzeBase, "crm").getAbsolutePath)

    val df = Reader.fromBronze(TestSpark.spark, DataSource.CRM, bronzeBase.getAbsolutePath)

    assertEquals(df.count(), 1L)

    val dlq = new File(bronzeBase, "crm/dlq")
    assert(dlq.exists())
    val dlqParquetFiles = Option(dlq.listFiles()).toSeq.flatten.flatMap { f =>
      if (f.isDirectory) Option(f.listFiles()).toSeq.flatten else Seq(f)
    }
    assert(dlqParquetFiles.exists(_.getName.endsWith(".snappy.parquet")))
  }
}
