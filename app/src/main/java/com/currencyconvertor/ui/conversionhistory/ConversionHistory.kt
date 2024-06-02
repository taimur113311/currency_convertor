package com.currencyconvertor.ui.conversionhistory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencyconvertor.domain.models.HistoricalDataModel
import com.currencyconvertor.ui.common.ErrorView
import com.currencyconvertor.ui.common.LoadingView
import com.currencyconvertor.ui.currencyconversion.CurrencyConversionUIState
import com.currencyconvertor.ui.theme.CurrencyConvertorTheme

@Composable
fun ConversionHistory() {
    val viewModel: ConversionHistoryViewModel = hiltViewModel()

    HistoricalDataScreen(
        fromCurrency = viewModel.fromCurrency,
        toCurrency = viewModel.toCurrency,
        viewModel = viewModel
    )
}

@Composable
fun HistoricalDataScreen(
    fromCurrency: String,
    toCurrency: String,
    viewModel: ConversionHistoryViewModel
) {
    val uiState by viewModel.uiState.observeAsState(initial = CurrencyConversionUIState.Loading)
    val loadingState by viewModel.loadingState.observeAsState(initial = true)
    val responseData by viewModel.responseData.observeAsState()

    when (uiState) {
        is CurrencyConversionUIState.Loading -> {
        }

        is CurrencyConversionUIState.ContentState -> {
        }

        is CurrencyConversionUIState.EmptyState -> {
            // Show empty state
            ErrorView(errorMessage = "No Data Available")
        }

        is CurrencyConversionUIState.Error -> {
            val errorMessage = (uiState as CurrencyConversionUIState.Error).message
            // Show error message
            ErrorView(errorMessage = errorMessage)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .background(color = Color.Green)
        ) {
            Text(text = "Chart")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Last3DaysHistoryList(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    isLoading = loadingState,
                    data = responseData
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Simple Text",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Composable
fun Last3DaysHistoryList(
    fromCurrency: String,
    toCurrency: String,
    isLoading: Boolean,
    data: List<HistoricalDataModel>?
) {
    if (isLoading) {
        LoadingView()
        Log.d("info test", "loading")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Last 3 days History",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Blue
            )
            Text(
                text = "$fromCurrency To $toCurrency",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                data?.let {
                    itemsIndexed(data) { index, entry ->
                        Text(
                            text = entry.date,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "1 $fromCurrency = ${entry.rate.format(4)} $toCurrency",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                    }
                }

            }
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

@Composable
@Preview(showBackground = true)
fun previewConversion() {
    CurrencyConvertorTheme {
        ConversionHistory()
    }
}
