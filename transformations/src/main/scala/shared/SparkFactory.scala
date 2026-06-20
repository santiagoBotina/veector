package com.veector
package shared

import org.apache.spark.sql.SparkSession

object SparkFactory {
  def create(name: String): SparkSession = {
    SparkSession.builder()
      .appName(name)
      .master("local[*]")
      .getOrCreate();
  }
}
