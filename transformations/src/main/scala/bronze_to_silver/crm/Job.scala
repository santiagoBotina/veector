package com.veector
package bronze_to_silver.crm

import shared.enums.DataSource

import com.veector.shared.{Reader, SparkFactory, Writer}
import io.github.cdimascio.dotenv.Dotenv

object Job {

  def main(args: Array[String]): Unit = {
    val spark =
      SparkFactory.create("crm-bronze-to-silver")

    val bronzeDf =
      Reader.fromBronze(spark, DataSource.CRM)

    val silverDf =
      Transformer.transform(bronzeDf)

    Writer.toSilver(silverDf, DataSource.CRM)

    spark.stop()
  }
}
