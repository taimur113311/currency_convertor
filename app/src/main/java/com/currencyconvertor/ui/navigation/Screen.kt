package com.currencyconvertor.ui.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object HistoryScreen : Screen("history_screen/{baseCurrency}/{fromCurrency}/{toCurrency}") {
        fun createRoute(baseCurrency: String, fromCurrency: String, toCurrency: String) =
            "history_screen/$baseCurrency/$fromCurrency/$toCurrency"
    }
}