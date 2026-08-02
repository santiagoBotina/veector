package com.veector
package shared

import org.apache.spark.sql.SparkSession

object SparkFactory {
  def create(name: String): SparkSession = {
    val spark = SparkSession.builder()
      .appName(name)
      .getOrCreate()

    val conf = spark.conf
    if (conf.getOption("spark.hadoop.fs.s3a.endpoint").isEmpty) {
      conf.set("spark.hadoop.fs.s3a.endpoint", sys.env.getOrElse("S3_URL", "http://minio:9000"))
      conf.set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
      conf.set("spark.hadoop.fs.s3a.access.key", sys.env.getOrElse("S3_ACCESS_KEY", "admin"))
      conf.set("spark.hadoop.fs.s3a.secret.key", sys.env.getOrElse("S3_SECRET_KEY", "password123"))
      conf.set("spark.hadoop.fs.s3a.path.style.access", "true")
    }

    spark
  }
}
