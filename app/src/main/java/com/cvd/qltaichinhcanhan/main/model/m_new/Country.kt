package com.cvd.qltaichinhcanhan.main.model.m_new

data class Country(
    val idCountry: Int? = 0,
    var countryName: String? = null,
    var currencyCode: String? = null,
    var currencyName: String? = null,
    var currencySymbol: String? = null,
    var flagUrl: String? = null,
    var exchangeRate: Float? = null,
    var selectCountry: Boolean? = null,
)
val countryList = listOf(
    Country(1, "United States", "USD", "United States Dollar", "$", "https://flagcdn.com/us.svg", 1.0f),
    Country(2, "European Union", "EUR", "Euro", "€", "https://flagcdn.com/eu.svg", 0.92f),
    Country(3, "Japan", "JPY", "Japanese Yen", "¥", "https://flagcdn.com/jp.svg", 146f),
    Country(4, "United Kingdom", "GBP", "British Pound", "£", "https://flagcdn.com/gb.svg", 0.79f),
    Country(5, "Switzerland", "CHF", "Swiss Franc", "CHF", "https://flagcdn.com/ch.svg", 0.88f),
    Country(6, "Australia", "AUD", "Australian Dollar", "$", "https://flagcdn.com/au.svg", 1.55f),
    Country(7, "Canada", "CAD", "Canadian Dollar", "$", "https://flagcdn.com/ca.svg", 1.35f),
    Country(8, "Việt Nam", "VND", "Vietnamese đồng", "₫", "https://flagcdn.com/vn.svg", 23965f)
)

