package com.example.financetracker.ui.theme

import androidx.compose.runtime.compositionLocalOf
import java.util.Locale

data class CurrencyInfo(
    val symbol: String = "₹",
    val locale: Locale = Locale("en", "IN")
)

val LocalCurrency = compositionLocalOf { CurrencyInfo() }

fun getCurrencyInfo(currency: String): CurrencyInfo {
    return when (currency.lowercase()) {
        "usd" -> CurrencyInfo("$", Locale.US)
        "yen" -> CurrencyInfo("¥", Locale.JAPAN)
        "rupee" -> CurrencyInfo("₹", Locale("en", "IN"))
        else -> CurrencyInfo("₹", Locale("en", "IN"))
    }
}
