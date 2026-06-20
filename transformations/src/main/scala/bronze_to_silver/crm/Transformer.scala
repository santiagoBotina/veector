package com.veector
package bronze_to_silver.crm

import org.apache.spark.sql.DataFrame

object Transformer {

  def transform(df: DataFrame): DataFrame = {
    // TODO: enrich this method with more transformations
    df
      .dropDuplicates()
  }
}
