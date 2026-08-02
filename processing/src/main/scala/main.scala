package com.veector

import org.apache.spark.sql.SparkSession

object main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("veector")
      .master("local[*]")
      .getOrCreate();

    val df = spark.read.csv("../sources/orders.csv")

    df.show()
  }
}
