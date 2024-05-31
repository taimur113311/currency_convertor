package com.currencyconvertor.data.remote

import com.currencyconvertor.BuildConfig
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConversionApi {
    @GET("https://run.mocky.io/v3/16a863a2-4da8-4b42-99d0-d36b212dc7c6")
    suspend fun getCurrencyResponse(
        @Query("access_key") accessKey: String? = BuildConfig.API_KEY
    ): CurrencyConversionResponse
}