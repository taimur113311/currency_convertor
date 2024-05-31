package com.currencyconvertor.data.remote.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rates(
    @Json(name = "AED")
    val aED: Double,
    @Json(name = "AFN")
    val aFN: Double
)