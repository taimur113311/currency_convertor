package com.currencyconvertor.data.remote

import com.currencyconvertor.BuildConfig
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyConversionApi {
//    @GET("https://run.mocky.io/v3/16a863a2-4da8-4b42-99d0-d36b212dc7c6") //Use this for mock Data
    @GET("/latest")
    suspend fun getCurrencyResponse(
        @Query("access_key") accessKey: String? = BuildConfig.API_KEY
    ): CurrencyConversionResponse

//    @GET("https://run.mocky.io/v3/dcb6b5da-a9a4-41de-82f5-61c17c77f39e/{date}")//Use this for mock Data
    @GET("/{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @Query("access_key") accessKey: String? = BuildConfig.API_KEY,
        @Query("base") base: String? = "EUR",
        @Query("symbols") symbols: String
    ): CurrencyConversionResponse
}