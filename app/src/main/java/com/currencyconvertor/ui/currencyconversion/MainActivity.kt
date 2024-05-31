package com.currencyconvertor.ui.currencyconversion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.currencyconvertor.R
import com.currencyconvertor.ui.theme.CurrencyConvertorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConvertorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverter()
                }
            }
        }
    }
}

@Composable
fun CurrencyConverter() {
    val currencies =
        listOf("USD", "EUR", "GBP", "INR", "AUD")
    var fromCurrency by remember { mutableStateOf(currencies[0]) }
    var toCurrency by remember { mutableStateOf(currencies[1]) }
    var amount by remember { mutableIntStateOf(1) }
    var convertedValue by remember {
        mutableFloatStateOf(
            convertCurrency(
                fromCurrency,
                toCurrency,
                amount
            )
        )
    }


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
                options = currencies,
                label = stringResource(R.string.from),
                onValueChangedEvent = { fromCurrency = it },
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
                    .clickable { }
            )
            DynamicSelectTextField(
                selectedValue = toCurrency,
                options = currencies,
                label = stringResource(R.string.to),
                onValueChangedEvent = { toCurrency = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            Log.d("info fromCurrency ", fromCurrency)
            Log.d("info toCurrency ", toCurrency)
        }
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
                onValueChange = {
                    if (it.isNotEmpty()) {
                        amount = it.toInt()
                        convertedValue = convertCurrency(fromCurrency, toCurrency, amount)
                    } else
                        amount = 1
                },
                label = { Text(stringResource(R.string.input_value)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )


            // Output value
            OutlinedTextField(
                value = convertedValue.toString(),
                onValueChange = { /* This field is read-only. */ },
                label = { Text(stringResource(R.string.output_value)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Convert button
        Button(
            onClick = {
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.details))
        }
    }
}


//Sample conversion code for design
fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Int): Float {
    return amount * 2.0f
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    selectedValue: String,
    options: List<String>,
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
            options.forEach { option: String ->
                DropdownMenuItem(text = { Text(text = option) }, onClick = {
                    expanded = false
                    onValueChangedEvent(option)
                })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyConvertorTheme {
        CurrencyConverter()
    }
}