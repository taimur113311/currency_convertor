package com.currencyconvertor.data.remote.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyConversionResponse(
    @Json(name = "base") val base: String,
    @Json(name = "date") val date: String,
    @Json(name = "rates") val rates: Map<String, Double>,
    @Json(name = "success") val success: Boolean,
    @Json(name = "timestamp") val timestamp: Long
)