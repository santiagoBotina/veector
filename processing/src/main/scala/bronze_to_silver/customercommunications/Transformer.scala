package com.veector
package bronze_to_silver.customercommunications

import com.veector.shared.Logger
import com.veector.shared.enums.DataSource
import com.veector.shared.silver.{Enricher, Normalizer}
import org.apache.spark.sql.DataFrame

object Transformer {
  def transform(df: DataFrame): DataFrame = {
    Logger.log(s"Starting transformation from bronze to silver layer - datasource: $DataSource.CustomerCommunications")

    val withoutDuplicates = df.dropDuplicates()

    val normalizedDF = Normalizer.applyTo(withoutDuplicates)

    Enricher.applyTo(normalizedDF, DataSource.CRM)
  }
}
