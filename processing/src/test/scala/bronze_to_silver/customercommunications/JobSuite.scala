package com.veector
package bronze_to_silver.customercommunications

import com.veector.shared.enums.DataSource
import munit.FunSuite

class JobSuite extends FunSuite {

  test("jobBuilder is wired to the CustomerCommunications source") {
    assertEquals(Job.jobBuilder().dataSource, Some(DataSource.CustomerCommunications))
  }

  test("jobBuilder derives the expected app name") {
    assertEquals(Job.jobBuilder().build().appName, "customer-communications-bronze-to-silver")
  }
}
