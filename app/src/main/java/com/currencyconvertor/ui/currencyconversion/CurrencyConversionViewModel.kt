package com.currencyconvertor.ui.currencyconversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyconvertor.data.onEmpty
import com.currencyconvertor.data.onError
import com.currencyconvertor.data.onLoading
import com.currencyconvertor.data.onSuccess
import com.currencyconvertor.data.remote.model.CurrencyConversionResponse
import com.currencyconvertor.domain.usecase.CurrencyConversionListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(private val currencyConversionListUseCase: CurrencyConversionListUseCase) :
    ViewModel() {

    private val _uiState = MutableLiveData<CurrencyConversionUIState>()
    val uiState: LiveData<CurrencyConversionUIState> = _uiState

    private val _responseData = MutableLiveData<CurrencyConversionResponse>()

    private val _fromCurrencyList = MutableLiveData<Map<String, Double>>()
    val fromCurrencyList: LiveData<Map<String, Double>> = _fromCurrencyList
    private val _toCurrencyList = MutableLiveData<Map<String, Double>>()
    val toCurrencyList: LiveData<Map<String, Double>> = _toCurrencyList

    private val _convertedAmount = MutableLiveData<Float>()
    val convertedAmount: LiveData<Float> = _convertedAmount

    private val _fromCurrency = MutableLiveData<String>()
    val fromCurrency: LiveData<String> = _fromCurrency
    private val _toCurrency = MutableLiveData<String>()
    val toCurrency: LiveData<String> = _toCurrency

    private val _amount = MutableLiveData<Int>().apply { value = 1 }
    val amount: LiveData<Int> = _amount

    init {
        loadCurrencyList()
    }

    private fun loadCurrencyList() {
        viewModelScope.launch {
            currencyConversionListUseCase().collect { dataResource ->
                dataResource.onSuccess {
                    if (this.data.success) {
                        _uiState.value = CurrencyConversionUIState.ContentState
                        _responseData.value = this.data
                        val rates = _responseData.value?.rates
                        val initialFromCurrency = rates?.get("EUR")?.let { "EUR" } ?: rates?.keys?.firstOrNull()
                        val initialToCurrency = rates?.get("USD")?.let { "USD" } ?: rates?.keys?.elementAtOrNull(1)

                        _fromCurrency.value = initialFromCurrency ?: ""
                        _toCurrency.value = initialToCurrency ?: ""
                        filterCurrencies()
                        updateConvertedAmount()
                    } else {
                        _uiState.value = CurrencyConversionUIState.EmptyState
                    }
                }.onLoading {
                    _uiState.value = CurrencyConversionUIState.Loading
                }.onEmpty {
                    _uiState.value = CurrencyConversionUIState.EmptyState
                }.onError {
                    _uiState.value = CurrencyConversionUIState.Error(this.exception.message!!)
                }
            }
        }
    }

    fun updateFromCurrency(newCurrency: String) {
        _fromCurrency.value = newCurrency
        filterCurrencies()
        updateConvertedAmount()
    }

    fun updateToCurrency(newCurrency: String) {
        _toCurrency.value = newCurrency
        filterCurrencies()
        updateConvertedAmount()
    }

    fun updateAmount(newAmount: Int) {
        _amount.value = newAmount
        updateConvertedAmount()
    }

    fun swapCurrencies() {
        val currentFrom = _fromCurrency.value
        val currentTo = _toCurrency.value
        _fromCurrency.value = currentTo!!
        _toCurrency.value = currentFrom!!
        filterCurrencies()
        updateConvertedAmount()
    }

    private fun filterCurrencies() {
        val fromCurrency = _fromCurrency.value
        val toCurrency = _toCurrency.value

        _fromCurrencyList.value = _responseData.value?.rates?.filterKeys { it != toCurrency }
        _toCurrencyList.value = _responseData.value?.rates?.filterKeys { it != fromCurrency }
    }

    private fun updateConvertedAmount() {

        val amount = _amount.value ?: 1
        val rates = _responseData.value?.rates
        val fromRate: Double? = rates?.get(_fromCurrency.value)
        val toRate: Double? = rates?.get(toCurrency.value)
        val baseCurrency: Double? = _responseData.value?.rates?.get(_responseData.value?.base)

        if (fromRate != null && toRate != null && baseCurrency != null) {
            _convertedAmount.value =
                (baseCurrency / fromRate * toRate * amount).toFloat()
        }
    }
}