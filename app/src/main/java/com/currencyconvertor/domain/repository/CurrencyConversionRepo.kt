package com.currencyconvertor.domain.repository

import com.currencyconvertor.data.DataResource
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import kotlinx.coroutines.flow.Flow

interface CurrencyConversionRepo {
    fun getCurrencyList(): Flow<DataResource<CurrencyConversionResponse>>

    fun getHistoricalData(date:String,currencySymbol:String):Flow<DataResource<CurrencyConversionResponse>>
}