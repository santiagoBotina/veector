package com.veector
package bronze_to_silver.marketingevents

import com.veector.shared.enums.DataSource
import munit.FunSuite

class JobSuite extends FunSuite {

  test("jobBuilder is wired to the MarketingEvents source") {
    assertEquals(Job.jobBuilder().dataSource, Some(DataSource.MarketingEvents))
  }

  test("jobBuilder derives the expected app name") {
    assertEquals(Job.jobBuilder().build().appName, "marketing-events-bronze-to-silver")
  }
}
