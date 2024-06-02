package com.currencyconvertor.domain.models

data class PopularCurrencyModel(
    val fromCurrency: String,
    val toCurrency: String,
    val fromCurrencyVal: Double,
    val toCurrencyVal: Double,
    val convertedAmount: Float
)