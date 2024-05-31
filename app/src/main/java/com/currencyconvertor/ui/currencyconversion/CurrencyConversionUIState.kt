package com.currencyconvertor.ui.currencyconversion

sealed class CurrencyConversionUIState {
    object Loading : CurrencyConversionUIState()
    object ContentState : CurrencyConversionUIState()
    object EmptyState : CurrencyConversionUIState()
    class Error(val message: String) : CurrencyConversionUIState()
}