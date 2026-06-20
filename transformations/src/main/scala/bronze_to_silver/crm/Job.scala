package com.veector
package bronze_to_silver.crm

import com.veector.shared.{SparkFactory, Reader, Writer}

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("crm-bronze-to-silver")

    val bronzeDf =
      Reader.csv(spark, "lalala")

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.write(silverDf, "lalala2")

    spark.stop()
  }
}
