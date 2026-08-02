package com.veector
package bronze_to_silver.orders

import com.veector.bronze_to_silver.TransformerPipelineSuite
import com.veector.shared.enums.DataSource

class TransformerSuite extends TransformerPipelineSuite(DataSource.Orders, Transformer.transform)
