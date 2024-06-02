package com.currencyconvertor.domain.usecase

import com.currencyconvertor.data.DataResource
import com.currencyconvertor.domain.models.HistoricalDataModel
import com.currencyconvertor.domain.repository.CurrencyConversionRepo
import com.currencyconvertor.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyHistoricalDataUseCase @Inject constructor(private val currencyConversionRepo: CurrencyConversionRepo) {
    operator fun invoke(
        baseCurrency: String,
        fromCurrency: String,
        toCurrency: String
    ): Flow<DataResource<List<HistoricalDataModel>>> {
        val currencySymbol = listOf(fromCurrency, toCurrency, baseCurrency).joinToString(",")

        val flows = AppUtils.getLastThreeDates().map { date ->
            currencyConversionRepo.getHistoricalData(date, currencySymbol)
        }

        return combine(flows) { results ->
            val historicalEntries = mutableListOf<HistoricalDataModel>()

            for (result in results) {
                if (result is DataResource.Success && result.data.success && result.data.historical == true) {
                    val rates = result.data.rates
                    val baseCurrencyValue = rates.getValue(baseCurrency)
                    val fromCurrencyValue = rates.getValue(fromCurrency)
                    val toCurrencyValue = rates.getValue(toCurrency)

                    val convertedAmount = baseCurrencyValue / fromCurrencyValue * toCurrencyValue
                    historicalEntries.add(HistoricalDataModel(result.data.date, convertedAmount))
                } else if (result is DataResource.Error<*>) {
                    return@combine DataResource.Error<Throwable>(result.exception)
                }
            }
            DataResource.Success(historicalEntries)
        }.flowOn(Dispatchers.IO)
            .map { it }
    }
}
