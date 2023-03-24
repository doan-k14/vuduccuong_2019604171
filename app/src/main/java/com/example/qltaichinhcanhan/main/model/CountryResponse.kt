package com.example.qltaichinhcanhan.main.model

data class CountryResponse(
    val name: String,
    val alpha2Code: String,
    val flag: String,
    val currencies: List<CurrencyResponse>
)
