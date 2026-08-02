package com.veector
package shared

import munit.FunSuite

class LoggerSuite extends FunSuite {

  test("log does not throw") {
    Logger.log("unit test message")
  }

  test("debug does not throw") {
    Logger.debug("unit test message")
  }

  test("warn does not throw") {
    Logger.warn("unit test message")
  }

  test("error does not throw") {
    Logger.error("unit test message")
  }

  test("error with a throwable does not throw") {
    Logger.error("unit test message", new RuntimeException("expected"))
  }
}
