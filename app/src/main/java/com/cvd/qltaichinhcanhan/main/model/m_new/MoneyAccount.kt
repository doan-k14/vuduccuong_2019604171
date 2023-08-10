package com.cvd.qltaichinhcanhan.main.model.m_new

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MoneyAccount(
    var idMoneyAccount: String = "",
    var moneyAccountName: String? = null,
    var amountMoneyAccount: Float? = null,
    var selectMoneyAccount: Boolean? = null,
    var idIcon: Int? = null,
    var idCountry: Int? = null,
    var idUserAccount: String? = null,
)
