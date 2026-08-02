package com.veector
package shared

import org.slf4j.{Logger => Slf4jLogger, LoggerFactory}

object Logger {
  private val logger: Slf4jLogger = LoggerFactory.getLogger(this.getClass.getName)

  def log(message: String): Unit = {
    if (logger.isInfoEnabled) {
      logger.info(message)
    }
  }

  def debug(message: String): Unit = {
    if (logger.isDebugEnabled) {
      logger.debug(message)
    }
  }

  def warn(message: String): Unit = {
    if (logger.isWarnEnabled) {
      logger.warn(message)
    }
  }

  def error(message: String): Unit = {
    if (logger.isErrorEnabled) {
      logger.error(message)
    }
  }

  def error(message: String, throwable: Throwable): Unit = {
    if (logger.isErrorEnabled) {
      logger.error(message, throwable)
    }
  }
}
