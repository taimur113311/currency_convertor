package com.currencyconvertor.domain.usecase

import com.currencyconvertor.data.DataResource
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import com.currencyconvertor.domain.repository.CurrencyConversionRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyConversionListUseCase @Inject constructor(private val currencyConversionRepo: CurrencyConversionRepo) {
    operator fun invoke(): Flow<DataResource<CurrencyConversionResponse>> =
        currencyConversionRepo.getCurrencyList()

}