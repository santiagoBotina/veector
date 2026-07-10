package com.veector
package bronze_to_silver.marketingevents

import com.veector.shared.{Reader, SparkFactory, Writer}
import shared.enums.DataSource

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("marketing-events-bronze-to-silver")

    val bronzeDf =
      Reader.fromBronze(spark, DataSource.MarketingEvents)

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.toSilver(silverDf, DataSource.MarketingEvents)

    spark.stop()
  }
}
