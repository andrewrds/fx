package com.andrewrds.fx

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    try {
        val parsedArgs = Args(args)
        val rate = retrieveRate(parsedArgs.ccy1, parsedArgs.ccy2)
        val convertedAmount = parsedArgs.amount * rate
        println(convertedAmount)
    } catch (e: Throwable) {
        System.err.println(e)
        exitProcess(-1)
    }
}

// Retrieve rate from api.fixer.io
private fun retrieveRate(ccy1: String, ccy2: String): BigDecimal {
    // The REST service doesn't give a rate for a currency to itself but obviously the rate is 1.00
    if (ccy1 == ccy2) return BigDecimal.ONE

    val url = URL("https://api.fixer.io/latest?base=$ccy1&symbols=$ccy2")
    val connection = url.openConnection() as HttpURLConnection
    try {
        return when (connection.responseCode) {
            200 -> extractJsonRate(connection, ccy2)
            422 -> handleJsonError(connection)
            else -> throw IOException(connection.responseMessage)
        }
    } finally {
        connection.disconnect()
    }
}

// Get rate from Json
private fun extractJsonRate(connection: HttpURLConnection, ccy: String): BigDecimal {
    val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
    try {
        val jsonObject: JsonObject? = JsonParser().parse(reader).asJsonObject
        val rates: JsonObject? = jsonObject?.getAsJsonObject("rates")
        return rates?.get(ccy)?.asBigDecimal ?: throw IOException("No rate information for $ccy")
    } finally {
        reader.close()
    }
}

// Get the error message from Json
private fun handleJsonError(connection: HttpURLConnection): BigDecimal {
    val reader = BufferedReader(InputStreamReader(connection.errorStream, "UTF-8"))
    try {
        val jsonObject: JsonObject? = JsonParser().parse(reader).asJsonObject
        val errorString = jsonObject?.get("error")
        throw IOException("Failure reading exchange rate: $errorString")
    } finally {
        reader.close()
    }
}



