package com.currencyconvertor.utils

fun Double.format(digits: Int) = "%.${digits}f".format(this)