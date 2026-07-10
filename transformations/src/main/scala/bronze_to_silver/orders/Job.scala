package com.veector
package bronze_to_silver.orders

import com.veector.shared.{Reader, SparkFactory, Writer}
import shared.enums.DataSource

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("orders-bronze-to-silver")

    val bronzeDf =
      Reader.fromBronze(spark, DataSource.Orders)

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.toSilver(silverDf, DataSource.Orders)

    spark.stop()
  }
}
