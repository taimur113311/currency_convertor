package com.currencyconvertor.ui.conversionhistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencyconvertor.R
import com.currencyconvertor.domain.models.HistoricalDataModel
import com.currencyconvertor.domain.models.PopularCurrencyModel
import com.currencyconvertor.ui.common.ErrorView
import com.currencyconvertor.ui.common.LoadingView
import com.currencyconvertor.ui.currencyconversion.CurrencyConversionUIState
import com.currencyconvertor.ui.theme.CurrencyConvertorTheme
import com.currencyconvertor.utils.format

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
    fromCurrency: String, toCurrency: String, viewModel: ConversionHistoryViewModel
) {
    val uiState by viewModel.uiState.observeAsState(initial = CurrencyConversionUIState.Loading)
    val loadingState by viewModel.loadingState.observeAsState(initial = true)
    val responseData by viewModel.responseData.observeAsState()
    val popularCurrencies by viewModel.popularCurrencies.observeAsState()

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
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
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
            ) {
                PopularCurrenciesConversion(
                    popularCurrencies = popularCurrencies,
                    isLoading = loadingState,
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
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.last_3_days_history),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
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
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                top = 10.dp, bottom = 10.dp
                            )
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun PopularCurrenciesConversion(
    popularCurrencies: List<PopularCurrencyModel>?,
    isLoading: Boolean
) {
    if (isLoading) {
        LoadingView()
    } else {
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)) {
            Text(
                text = stringResource(R.string.popular_currencies_conversion),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                color = Color.Blue
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    itemContent = { index ->
                        popularCurrencies?.get(index)?.let { conversion ->
                            ConversionItem(conversion)
                        }
                    },
                    count = popularCurrencies?.size ?: 0
                )
            }
        }

    }

}

@Composable
fun ConversionItem(conversion: PopularCurrencyModel) {
    val fromCurrency = conversion.fromCurrency

    val fromValue = conversion.fromCurrencyVal.format(4)
    val toCurrency = conversion.toCurrency
    val toValue = conversion.toCurrencyVal.format(4)
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(bottom = 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            )
            {
                Text(
                    text = fromCurrency,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward),
                    contentDescription = "direction_from_to"
                )
                Text(
                    text = toCurrency,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            )
            {
                Text(
                    text = fromValue,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = toValue,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }


}

@Composable
@Preview(showBackground = true)
fun previewConversion() {
    CurrencyConvertorTheme {
        ConversionHistory()
    }
}
