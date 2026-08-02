package com.veector
package shared.silver

import bronze_to_silver.crm.{Schema => CrmSchemaObj}
import com.veector.shared.TestSpark
import com.veector.shared.enums.DataSource
import com.veector.shared.Reader
import com.veector.shared.bronze.Writer
import munit.FunSuite

import java.io.File
import java.nio.file.Files

class JobBuilderSuite extends FunSuite {

  test("runs the bronze-to-silver pipeline read-transform-write steps against a local file system") {
    val tmp = Files.createTempDirectory("veector-pipeline").toFile
    val bronzeBase = new File(tmp, "bronze")
    val silverBase = new File(tmp, "silver")

    val valid = TestSpark.defaultValuesFor(CrmSchemaObj.CRMSchema)
    val bronze = TestSpark.dataFrameFromRows(CrmSchemaObj.CRMSchema, Seq(valid, valid))
    bronze.write.mode("overwrite").parquet(new File(bronzeBase, "crm").getAbsolutePath)

    val read = Reader.fromBronze(TestSpark.spark, DataSource.CRM, bronzeBase.getAbsolutePath)
    val silver = TransformerBuilder().withDataSource(DataSource.CRM).transform(read)
    Writer.toSilver(silver, DataSource.CRM, silverBase.getAbsolutePath)

    val result = TestSpark.spark.read.parquet(new File(silverBase, "crm").getAbsolutePath)
    assertEquals(result.count(), 1L)
    assertEquals(result.select("source_system").distinct().collect().head.getString(0), DataSource.CRM.toString)
    assert(result.columns.contains("processing_timestamp"))
  }

  test("default builder starts unconfigured") {
    assertEquals(JobBuilder().dataSource, None)
    assertEquals(JobBuilder().appName, None)
  }

  test("withDataSource sets the source") {
    assertEquals(JobBuilder().withDataSource(DataSource.Orders).dataSource, Some(DataSource.Orders))
  }

  test("withAppName overrides the derived default") {
    val job = JobBuilder().withDataSource(DataSource.CRM).withAppName("custom-job").build()
    assertEquals(job.appName, "custom-job")
  }

  test("build derives the kebab-case app name for every supported source") {
    val expected = Map(
      DataSource.CRM -> "crm-bronze-to-silver",
      DataSource.CustomerCommunications -> "customer-communications-bronze-to-silver",
      DataSource.MarketingEvents -> "marketing-events-bronze-to-silver",
      DataSource.Orders -> "orders-bronze-to-silver",
      DataSource.SupportTickets -> "support-tickets-bronze-to-silver"
    )

    expected.foreach { case (source, appName) =>
      assertEquals(JobBuilder().withDataSource(source).build().appName, appName)
    }
  }

  test("build preserves the configured source") {
    val job = JobBuilder().withDataSource(DataSource.SupportTickets).build()
    assertEquals(job.dataSource, DataSource.SupportTickets)
  }

  test("build throws when dataSource is missing") {
    intercept[IllegalStateException] {
      JobBuilder().build()
    }
  }

  test("run throws when dataSource is missing, before any Spark work happens") {
    intercept[IllegalStateException] {
      JobBuilder().run()
    }
  }

  test("kebabCase converts CamelCase and acronyms") {
    assertEquals(JobBuilder.kebabCase("CRM"), "crm")
    assertEquals(JobBuilder.kebabCase("CustomerCommunications"), "customer-communications")
    assertEquals(JobBuilder.kebabCase("MarketingEvents"), "marketing-events")
    assertEquals(JobBuilder.kebabCase("Orders"), "orders")
    assertEquals(JobBuilder.kebabCase("SupportTickets"), "support-tickets")
    assertEquals(JobBuilder.kebabCase(""), "")
  }
}
