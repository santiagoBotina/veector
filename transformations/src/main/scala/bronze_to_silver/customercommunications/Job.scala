package com.veector
package bronze_to_silver.customercommunications

import com.veector.shared.{Reader, SparkFactory, Writer}
import shared.enums.DataSource

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("customer-communications-bronze-to-silver")

    val bronzeDf =
      Reader.fromBronze(spark, DataSource.CustomerCommunications)

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.toSilver(silverDf, DataSource.CustomerCommunications)

    spark.stop()
  }
}
