package com.currencyconvertor.data.remote

import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CurrencyConversionApi {
    @POST("latest")
    suspend fun getCurrencyResponse(
    ): CurrencyConversionResponse
}