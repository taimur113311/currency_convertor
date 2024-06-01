package com.currencyconvertor.ui.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object HistoryScreen : Screen("history_screen/{fromCurrency}/{toCurrency}") {
        fun createRoute(fromCurrency: String, toCurrency: String) = "history_screen/$fromCurrency/$toCurrency"
    }
}