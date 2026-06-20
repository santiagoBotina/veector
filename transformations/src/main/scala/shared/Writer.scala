package com.veector
package shared

import org.apache.spark.sql.DataFrame

object Writer {
  def write(df: DataFrame, parquetFilePath: String): Unit = {
    df.write
      .mode("overwrite")
      .parquet(parquetFilePath)
  }
}
