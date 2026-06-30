package com.veector
package shared

import io.github.cdimascio.dotenv.Dotenv
import org.apache.spark.sql.SparkSession

object SparkFactory {
  def create(name: String): SparkSession = {
    val dotenv = Dotenv.load()

    SparkSession.builder()
      .appName(name)
      .master("local[*]")
      .config("spark.hadoop.fs.s3a.endpoint", dotenv.get("S3_ENDPOINT"))
      .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
      .config("spark.hadoop.fs.s3a.access.key", dotenv.get("S3_ACCESS_KEY"))
      .config("spark.hadoop.fs.s3a.secret.key", dotenv.get("S3_SECRET_KEY"))
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .getOrCreate();
  }
}
