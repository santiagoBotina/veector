package com.veector
package shared.bronze

import shared.enums.DataSource

import org.apache.spark.sql.DataFrame

object Writer {
  val silverBasePath: String = "s3a://veector-lakehouse/silver"

  def toSilver(df: DataFrame, dataSource: DataSource, basePath: String = silverBasePath): Unit = {
    dataSource match
    {
      case DataSource.CRM => write(df, s"$basePath/crm")
      case DataSource.CustomerCommunications => write(df, s"$basePath/communications")
      case DataSource.MarketingEvents => write(df, s"$basePath/marketing")
      case DataSource.Orders => write(df, s"$basePath/orders")
      case DataSource.SupportTickets => write(df, s"$basePath/support")
    }
  }

  def write(df: DataFrame, parquetFilePath: String): Unit = {
    // This makes a full overwrite, would be good to implement partitioned writing for silver stage
    df.write
      .mode("overwrite")
      .parquet(parquetFilePath)
  }
}
