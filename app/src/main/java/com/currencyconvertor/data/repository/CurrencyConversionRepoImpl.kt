package com.currencyconvertor.data.repository

import com.currencyconvertor.BuildConfig
import com.currencyconvertor.data.DataResource
import com.currencyconvertor.data.callApi
import com.currencyconvertor.data.remote.CurrencyConversionApi
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import com.currencyconvertor.domain.repository.CurrencyConversionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyConversionRepoImpl @Inject constructor(
    private val currencyConversionApi: CurrencyConversionApi
) : CurrencyConversionRepo {
    override fun getCurrencyList(): Flow<DataResource<CurrencyConversionResponse>> = flow {
        emit(DataResource.Loading)
        val result =
            callApi { currencyConversionApi.getCurrencyResponse() }
        emit(result)
    }.flowOn(Dispatchers.IO)
}