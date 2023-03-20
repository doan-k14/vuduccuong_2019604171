package com.example.qltaichinhcanhan.main.m

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,
    val name: String?=null,
    val currencyCode: String?=null,
    val currencyName: String?=null,
    val currencySymbol: String?=null,
    val flagUrl: String?=null
)

