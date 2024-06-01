package com.currencyconvertor.ui.conversionhistory

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConversionHistory(fromCurrency:String, toCurrency:String) {
    Text(text = "test $fromCurrency, $toCurrency")
}
