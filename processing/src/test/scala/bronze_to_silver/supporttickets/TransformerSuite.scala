package com.veector
package bronze_to_silver.supporttickets

import com.veector.bronze_to_silver.TransformerPipelineSuite
import com.veector.shared.enums.DataSource

class TransformerSuite extends TransformerPipelineSuite(DataSource.SupportTickets, Transformer.transform)
