package com.example.qltaichinhcanhan.main.model.m_r

import android.os.Parcelable
import androidx.room.ColumnInfo
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
            parentColumns = ["id"],
            childColumns = ["idCountry"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MoneyAccount(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    var moneyAccountName: String? = null,
    var amountMoneyAccount: Float? = null,
    var select: Boolean? = null,
    var icon: Int? = null,
    var color: Int? = null,
    var idCountry: Int? = null,
    var idAccount: Int? = null,
) : Parcelable
