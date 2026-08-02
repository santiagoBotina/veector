package com.veector
package bronze_to_silver.supporttickets

import com.veector.shared.enums.DataSource
import munit.FunSuite

class JobSuite extends FunSuite {

  test("jobBuilder is wired to the SupportTickets source") {
    assertEquals(Job.jobBuilder().dataSource, Some(DataSource.SupportTickets))
  }

  test("jobBuilder derives the expected app name") {
    assertEquals(Job.jobBuilder().build().appName, "support-tickets-bronze-to-silver")
  }
}
