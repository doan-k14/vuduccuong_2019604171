package com.example.qltaichinhcanhan.main.model.m_r

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idAccount")
    val idAccount: Int = 0,
    var accountName: String? = null,
    var emailName: String? = null,
    var password: String? = null,
    var urlImage: String? = null,
    var selectAccount: Boolean? = null,
)
