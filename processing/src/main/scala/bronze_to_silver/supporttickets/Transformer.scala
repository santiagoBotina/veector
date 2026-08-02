package com.veector
package bronze_to_silver.supporttickets

import com.veector.shared.enums.DataSource
import com.veector.shared.silver.TransformerBuilder
import org.apache.spark.sql.DataFrame

object Transformer {
  def transform(df: DataFrame): DataFrame =
    TransformerBuilder()
      .withDataSource(DataSource.SupportTickets)
      .transform(df)
}
