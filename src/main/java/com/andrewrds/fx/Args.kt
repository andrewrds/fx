package com.andrewrds.fx

import java.math.BigDecimal
import kotlin.system.exitProcess

/**
 * Command line arguments
 */
internal class Args(args: Array<String>) {
    val amount: BigDecimal = if (args.size == 3) BigDecimal(args[0]) else BigDecimal.ONE
    val ccy1 = if (args.size >= 2) args[args.size - 2] else ""
    val ccy2 = if (args.size >= 2) args.last() else ""

    init {
        if (args.size < 2) {
            System.err.println("Expected parameters [amount] currency1 currency2")
            exitProcess(-1)
        }
    }
}
