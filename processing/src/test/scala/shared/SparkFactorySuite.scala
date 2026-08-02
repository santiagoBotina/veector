package com.veector
package shared

import munit.FunSuite

class SparkFactorySuite extends FunSuite {

  test("create returns a live session") {
    assert(!TestSpark.spark.sparkContext.isStopped)
  }

  test("create uses the provided app name") {
    assertEquals(TestSpark.spark.sparkContext.appName, "test-app")
  }

  test("create defaults the S3A endpoint and credentials from the environment") {
    val conf = TestSpark.spark.conf

    assertEquals(
      conf.get("spark.hadoop.fs.s3a.endpoint"),
      sys.env.getOrElse("S3_URL", "http://minio:9000")
    )
    assertEquals(
      conf.get("spark.hadoop.fs.s3a.access.key"),
      sys.env.getOrElse("S3_ACCESS_KEY", "admin")
    )
    assertEquals(
      conf.get("spark.hadoop.fs.s3a.secret.key"),
      sys.env.getOrElse("S3_SECRET_KEY", "password123")
    )
    assertEquals(conf.get("spark.hadoop.fs.s3a.path.style.access"), "true")
  }
}
