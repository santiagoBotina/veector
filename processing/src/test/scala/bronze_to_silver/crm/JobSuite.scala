package com.veector
package bronze_to_silver.crm

import com.veector.shared.enums.DataSource
import munit.FunSuite

class JobSuite extends FunSuite {

  test("jobBuilder is wired to the CRM source") {
    assertEquals(Job.jobBuilder().dataSource, Some(DataSource.CRM))
  }

  test("jobBuilder derives the expected app name") {
    assertEquals(Job.jobBuilder().build().appName, "crm-bronze-to-silver")
  }
}
