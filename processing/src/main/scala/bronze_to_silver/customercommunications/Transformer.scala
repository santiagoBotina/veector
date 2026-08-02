package com.veector
package bronze_to_silver.customercommunications

import com.veector.shared.enums.DataSource
import com.veector.shared.silver.TransformerBuilder
import org.apache.spark.sql.DataFrame

object Transformer {
  def transform(df: DataFrame): DataFrame =
    TransformerBuilder()
      .withDataSource(DataSource.CustomerCommunications)
      .transform(df)
}
