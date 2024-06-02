package com.currencyconvertor.ui.conversionhistory

sealed class CurrencyHistoryUIState {
    object Loading : CurrencyHistoryUIState()
    object ContentState : CurrencyHistoryUIState()
    object EmptyState : CurrencyHistoryUIState()
    class Error(val message: String) : CurrencyHistoryUIState()
}