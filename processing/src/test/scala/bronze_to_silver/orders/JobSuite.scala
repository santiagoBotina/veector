package com.veector
package bronze_to_silver.orders

import com.veector.shared.enums.DataSource
import munit.FunSuite

class JobSuite extends FunSuite {

  test("jobBuilder is wired to the Orders source") {
    assertEquals(Job.jobBuilder().dataSource, Some(DataSource.Orders))
  }

  test("jobBuilder derives the expected app name") {
    assertEquals(Job.jobBuilder().build().appName, "orders-bronze-to-silver")
  }
}
