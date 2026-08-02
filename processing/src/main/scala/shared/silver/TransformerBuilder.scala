package com.veector
package shared.silver

import com.veector.shared.Logger
import com.veector.shared.enums.DataSource
import org.apache.spark.sql.DataFrame

/**
 * Builder for the bronze-to-silver transformation pipeline.
 *
 * The produced pipeline applies, in order: deduplication, normalization and enrichment tagged with
 * the configured `dataSource`. The only mandatory field is `dataSource`.
 */
case class TransformerBuilder(dataSource: Option[DataSource] = None) {
  def withDataSource(dataSource: DataSource): TransformerBuilder = copy(dataSource = Some(dataSource))

  def build(): DataFrame => DataFrame = {
    val source = dataSource.getOrElse(
      throw new IllegalStateException("dataSource is required to build a transformer")
    )

    (df: DataFrame) => {
      Logger.log(s"Starting transformation from bronze to silver layer - datasource: $source")

      val withoutDuplicates = df.dropDuplicates()
      val normalizedDF = Normalizer.applyTo(withoutDuplicates)

      Enricher.applyTo(normalizedDF, source)
    }
  }

  def transform(df: DataFrame): DataFrame = build()(df)
}
