package com.veector
package shared.enums

sealed trait DataSource

object DataSource {
  case object CRM extends DataSource

  case object CustomerCommunications extends DataSource

  case object MarketingEvents extends DataSource

  case object Orders extends DataSource

  case object SupportTickets extends DataSource
}
