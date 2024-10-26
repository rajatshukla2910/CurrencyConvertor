package com.convertor.data

data class ExchangeRatesData(
    val base: String,
    val rates: Map<String, Double>
)
