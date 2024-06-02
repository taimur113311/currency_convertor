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
import com.currencyconvertor.domain.usecase.CurrencyHistoricalDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionHistoryViewModel @Inject constructor(
    private val currencyHistoricalDataUseCase: CurrencyHistoricalDataUseCase,
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

    init {
        callHistoryApi()

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
}