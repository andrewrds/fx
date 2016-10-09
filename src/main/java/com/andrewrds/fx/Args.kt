package com.andrewrds.fx

import java.math.BigDecimal

/**
 * Command line arguments
 */
internal class Args(args: Array<String>) {
    val amount = if (args.size == 3) BigDecimal(args[0]) else BigDecimal.ONE
    val ccy1 = args[args.size - 2]
    val ccy2 = args.last()

    init {

    }
}
