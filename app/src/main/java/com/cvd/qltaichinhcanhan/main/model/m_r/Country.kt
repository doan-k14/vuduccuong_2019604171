package com.cvd.qltaichinhcanhan.main.model.m_r

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idCountry")
    val idCountry: Int? = 0,
    var countryName: String? = null,
    var currencyCode: String? = null,
    var currencyName: String? = null,
    var currencySymbol: String? = null,
    var flagUrl: String? = null,
    var exchangeRate: Float? = null,
    var selectCountry: Boolean? = null,
)
val countries: List<Country> = listOf(
    Country(1, "United States", "USD", "United States Dollar", "$", "https://flagcdn.com/us.svg", 1.0f),
    Country(2, "European Union", "EUR", "Euro", "€", "https://flagcdn.com/eu.svg", 0.85f),
    Country(3, "Japan", "JPY", "Japanese Yen", "¥", "https://flagcdn.com/jp.svg", 110.0f),
    Country(4, "United Kingdom", "FBP", "British Pound", "£", "https://flagcdn.com/uk.svg", 0.72f),
    Country(5, "Canada", "CAD", "Canadian Dollar", "$", "https://flagcdn.com/ca.svg", 1.24f),
    Country(6, "Switzerland", "CHF", "Swiss Franc", "CHF", "https://flagcdn.com/ch.svg", 0.91f),
    Country(7, "Australia", "AUD", "Australian Dollar", "$", "https://flagcdn.com/au.svg", 1.37f),
    Country(8, "China", "CNY", "Chinese Yuan", "¥", "https://flagcdn.com/cn.svg", 6.47f),
    Country(9, "South Korea", "KRW", "South Korean Won", "₩", "https://flagcdn.com/kr.svg", 1173.0f),
    Country(10, "Singapore", "SGD", "Singapore Dollar", "$", "https://flagcdn.com/sg.svg", 1.33f),
    Country(11, "Hong Kong", "HKD", "Hong Kong Dollar", "$", "https://flagcdn.com/hk.svg", 7.78f),
    Country(12, "Russia", "RUB", "Russian Ruble", "₽", "https://flagcdn.com/ru.svg", 72.92f),
    Country(13, "Brazil", "BRL", "Brazilian Real", "R$", "https://flagcdn.com/br.svg", 5.16f),
    Country(14, "Turkey", "TRY", "Turkish Lira", "₺", "https://flagcdn.com/tr.svg", 13.50f),
    Country(15, "India", "INR", "Indian Rupee", "₹", "https://flagcdn.com/in.svg", 73.5f),
    Country(16, "Mexico", "MXN", "Mexican Peso", "$", "https://flagcdn.com/mx.svg", 20.1f),
    Country(17, "Kuwait", "KWD", "Kuwaiti Dinar", "د.ك", "https://flagcdn.com/kw.svg", 0.30f),
    Country(18, "Saudi Arabia", "SAR", "Saudi Riyal", "﷼", "https://flagcdn.com/sa.svg", 3.75f),
    Country(19, "Thailand", "THB", "Thai Baht", "฿", "https://flagcdn.com/th.svg", 32.9f),
    Country(20, "Norway", "NOK", "Norwegian Krone", "kr", "https://flagcdn.com/no.svg", 9.16f),
    Country(21, "Việt Nam", "VND", "Vietnamese đồng", "₫", "https://flagcdn.com/vn.svg", 9.16f)
)
