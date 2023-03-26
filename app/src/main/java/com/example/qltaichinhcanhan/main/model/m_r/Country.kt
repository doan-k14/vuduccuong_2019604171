package com.example.qltaichinhcanhan.main.model.m_r

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    var countryName: String? = null,
    var currencyCode: String? = null,
    var currencyName: String? = null,
    var currencySymbol: String? = null,
    var flagUrl: String? = null,
    var exchangeRate: Float? = null,
    var select: Boolean? = null,
)

