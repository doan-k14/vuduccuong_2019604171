package com.example.qltaichinhcanhan.main.model.m_r

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "moneyAccount",
    foreignKeys = [
        ForeignKey(
            entity = Country::class,
            parentColumns = ["idCountry"],
            childColumns = ["idCountry"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["idAccount"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MoneyAccount(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idMoneyAccount")
    var idMoneyAccount: Int = 0,
    var moneyAccountName: String? = null,
    var amountMoneyAccount: Float? = null,
    var selectMoneyAccount: Boolean? = null,
    var icon: Int? = null,
    var color: Int? = null,
    var idCountry: Int? = null,
    var idAccount: Int? = null,
) : Parcelable
