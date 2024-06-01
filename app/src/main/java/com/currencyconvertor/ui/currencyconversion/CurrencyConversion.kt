package com.currencyconvertor.ui.currencyconversion

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencyconvertor.R
import com.currencyconvertor.ui.common.ErrorView
import com.currencyconvertor.ui.common.LoadingView

@Composable
fun CurrencyConverterScreen(
    viewModel: CurrencyConversionViewModel = hiltViewModel(),
    detailsScreen: (String, String) -> Unit,
) {
    val uiState by viewModel.uiState.observeAsState(initial = CurrencyConversionUIState.Loading)


    when (uiState) {
        is CurrencyConversionUIState.Loading -> {
            LoadingView()
        }

        is CurrencyConversionUIState.ContentState -> {
            ShowCurrencyConvertorView(
                viewModel = viewModel,
                detailsScreen = detailsScreen
            )
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

}

@Composable
private fun ShowCurrencyConvertorView(
    viewModel: CurrencyConversionViewModel,
    detailsScreen: (String, String) -> Unit,

    ) {
    val fromCurrencyList by viewModel.fromCurrencyList.observeAsState()
    val toCurrencyList by viewModel.toCurrencyList.observeAsState()
    val convertedAmount by viewModel.convertedAmount.observeAsState()
    val fromCurrency by viewModel.fromCurrency.observeAsState("")
    val toCurrency by viewModel.toCurrency.observeAsState("")

    val amount by viewModel.amount.observeAsState(1)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 60.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DynamicSelectTextField(
                selectedValue = fromCurrency,
                options = fromCurrencyList?.keys?.toList(),
                label = stringResource(R.string.from),
                onValueChangedEvent = { newCurrency ->
                    viewModel.updateFromCurrency(newCurrency)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_swap_currency), // Replace with your image resource
                contentDescription = "Exchange Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(48.dp)
                    .padding(horizontal = 8.dp)
                    .clickable { viewModel.swapCurrencies() }
            )
            DynamicSelectTextField(
                selectedValue = toCurrency,
                options = toCurrencyList?.keys?.toList(),
                label = stringResource(R.string.to),
                onValueChangedEvent = { newCurrency ->
                    viewModel.updateToCurrency(newCurrency)

                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
        InputOutputView(
            amount = amount,
            convertedAmount = convertedAmount,
            updateAmount = { newAmount ->
                viewModel.updateAmount(newAmount)
            })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { detailsScreen(fromCurrency,toCurrency) },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.details),
                )
            }
        }
    }

}

@Composable
fun InputOutputView(
    amount: Int, convertedAmount: Float?,
    updateAmount: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Input value
        OutlinedTextField(
            value = amount.toString(),
            onValueChange = { newAmount ->
                val parsedAmount = newAmount.toIntOrNull() ?: 1
                updateAmount(parsedAmount)
            },
            label = { Text(stringResource(R.string.input_value)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )

        // Output value
        convertedAmount?.let {
            OutlinedTextField(
                value = it.toString(),
                onValueChange = { /* This field is read-only. */ },
                readOnly = true,
                label = { Text(stringResource(R.string.output_value)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    selectedValue: String,
    options: List<String>?,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options?.forEach { option: String ->
                DropdownMenuItem(text = { Text(text = option) }, onClick = {
                    expanded = false
                    onValueChangedEvent(option)
                })
            }
        }
    }
}
