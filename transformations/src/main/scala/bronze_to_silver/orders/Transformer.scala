package com.veector
package bronze_to_silver.orders

import org.apache.spark.sql.DataFrame

object Transformer {

  def transform(df: DataFrame): DataFrame = {
    df
      .dropDuplicates()
  }
}
