package com.example.currency

data class currencyResponse(
    val rates: Map<String, Double>,
    val base: String,
    val date: String
)