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
                        filterCurrencies(_responseData.value?.rates?.keys?.first()) //By Default selected currency will be first one


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

    fun filterCurrencies(selectedCurrency: String?) {
        val rates = _responseData.value!!.rates
        _fromCurrencyList.value = rates
        _toCurrencyList.value =
            rates.filter { it.key != selectedCurrency }
    }
}