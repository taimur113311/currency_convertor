package com.currencyconvertor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencyconvertor.ui.conversionhistory.ConversionHistory
import com.currencyconvertor.ui.currencyconversion.CurrencyConverterScreen
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(
            route = Screen.MainScreen.route,
            enterTransition = null,
            exitTransition = null
        ) {
            CurrencyConverterScreen(detailsScreen = { fromCurrency, toCurrency ->
                navController.navigate(Screen.HistoryScreen.createRoute(fromCurrency, toCurrency))
            })
        }
        composable(
            route = Screen.HistoryScreen.route,
            enterTransition = null,
            exitTransition = null
        ) { backStackEntry ->
            val fromCurrency = backStackEntry.arguments?.getString("fromCurrency") ?: ""
            val toCurrency = backStackEntry.arguments?.getString("toCurrency") ?: ""
            ConversionHistory(fromCurrency = fromCurrency, toCurrency = toCurrency)
        }
    }
}
