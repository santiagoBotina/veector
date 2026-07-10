package com.veector
package bronze_to_silver.supporttickets

import com.veector.shared.{Reader, SparkFactory, Writer}
import shared.enums.DataSource

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("support-tickets-bronze-to-silver")

    val bronzeDf =
      Reader.fromBronze(spark, DataSource.SupportTickets)

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.toSilver(silverDf, DataSource.SupportTickets)

    spark.stop()
  }
}
