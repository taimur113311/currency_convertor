package com.currencyconvertor.ui.conversionhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyconvertor.data.onEmpty
import com.currencyconvertor.data.onError
import com.currencyconvertor.data.onLoading
import com.currencyconvertor.data.onSuccess
import com.currencyconvertor.domain.models.HistoricalDataModel
import com.currencyconvertor.domain.models.PopularCurrencyModel
import com.currencyconvertor.domain.usecase.CurrencyConversionListUseCase
import com.currencyconvertor.domain.usecase.CurrencyHistoricalDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionHistoryViewModel @Inject constructor(
    private val currencyHistoricalDataUseCase: CurrencyHistoricalDataUseCase,
    private val currencyConversionListUseCase: CurrencyConversionListUseCase,
    savedStateHandle: SavedStateHandle,
) :
    ViewModel() {
    val baseCurrency: String = savedStateHandle["baseCurrency"] ?: ""
    val fromCurrency: String = savedStateHandle["fromCurrency"] ?: ""
    val toCurrency: String = savedStateHandle["toCurrency"] ?: ""

    private val _uiState = MutableLiveData<CurrencyHistoryUIState>()
    val uiState: LiveData<CurrencyHistoryUIState> = _uiState
    private val _responseData = MutableLiveData<List<HistoricalDataModel>>()
    val responseData: LiveData<List<HistoricalDataModel>> = _responseData

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _popularCurrencies = MutableLiveData<List<PopularCurrencyModel>>()
    val popularCurrencies = _popularCurrencies as LiveData<List<PopularCurrencyModel>>
    init {
        callHistoryApi()
        loadCurrencyList()
    }

    private fun callHistoryApi() {
        viewModelScope.launch {
            _loadingState.value = true
            currencyHistoricalDataUseCase(
                baseCurrency = baseCurrency,
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            ).collect { dataResource ->
                dataResource.onSuccess {
                    _responseData.value = this.data
                    _uiState.value = CurrencyHistoryUIState.ContentState
                    _loadingState.value = false
                }.onLoading {
                    _loadingState.value = true
                    _uiState.value = CurrencyHistoryUIState.Loading
                }.onEmpty {
                    _uiState.value = CurrencyHistoryUIState.EmptyState
                }.onError {
                    _uiState.value = CurrencyHistoryUIState.Error(this.exception.message!!)
                }
            }
        }

    }

    private fun loadCurrencyList() {
        viewModelScope.launch {
            _loadingState.value = true
            currencyConversionListUseCase().collect { dataResource ->
                dataResource.onSuccess {
                    if (this.data.success) {
                        _uiState.value = CurrencyHistoryUIState.ContentState
                        _popularCurrencies.value =
                            getConvertedPopularCurrencies(this.data.rates, this.data.base)
                        _loadingState.value = false
                    } else {
                        _uiState.value = CurrencyHistoryUIState.EmptyState
                    }
                }.onLoading {
                    _loadingState.value = true
                    _uiState.value = CurrencyHistoryUIState.Loading

                }.onEmpty {
                    _uiState.value = CurrencyHistoryUIState.EmptyState
                }.onError {
                    _uiState.value = CurrencyHistoryUIState.Error(this.exception.message!!)
                }
            }
        }
    }


    private fun applyConversion(
        baseCurrency: Double,
        fromRate: Double,
        toRate: Double,
        amount: Int
    ): Float {

        return (baseCurrency / fromRate * toRate * amount).toFloat()
    }


    private fun getConvertedPopularCurrencies(currencies: Map<String,Double>,base: String): List<PopularCurrencyModel> {
        val baseCurrencyValue = currencies[base] ?: 1.0
        val popularCurrencies: Map<String, Double> =
            currencies.filterKeys {
                it in listOf("USD", "GBP", "CNY","JPY", "AUD", "CAD", "CHF", "SEK", "NZD", "SGD")
            }
        // Create model [from currency, to currency, converted amount] for each pair of popular currencies
        val convertedPopularCurrencies = popularCurrencies.keys.map { currency ->
            val toCurrencyValue = currencies[currency]!!
            val convertedAmount =
                applyConversion(baseCurrencyValue, baseCurrencyValue, toCurrencyValue, 1)
            PopularCurrencyModel(base, currency, baseCurrencyValue, toCurrencyValue, convertedAmount)
        }
        return convertedPopularCurrencies

    }
}