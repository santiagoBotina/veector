package com.veector
package bronze_to_silver.orders

import com.veector.shared.enums.DataSource
import com.veector.shared.silver.JobBuilder

object Job {

  def main(args: Array[String]): Unit = jobBuilder().run()

  def jobBuilder(): JobBuilder = JobBuilder().withDataSource(DataSource.Orders)
}
