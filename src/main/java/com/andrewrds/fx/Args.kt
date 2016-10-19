package com.andrewrds.fx

import java.math.BigDecimal
import kotlin.system.exitProcess

/**
 * Command line arguments
 */
internal class Args(args: Array<String>) {
    val amount: BigDecimal = if (args.size == 3) BigDecimal(args[0]) else BigDecimal.ONE
    val ccy1 = args[args.size - 2]
    val ccy2 = args.last()
}
