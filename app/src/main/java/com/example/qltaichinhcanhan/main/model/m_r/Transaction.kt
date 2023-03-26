package com.example.qltaichinhcanhan.main.model.m_r

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transaction",
    foreignKeys = [
        ForeignKey(entity = MoneyAccount::class,
            parentColumns = ["id"],
            childColumns = ["idMoneyAccount"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE)
    ])

data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    var transactionName: String? = null,
    var transactionAmount: Float? = null,
    var day: Long? = null,
    var comment: String? = null,
    var select: Boolean? = null,
    var idMoneyAccount: Int? = null,
    var idCategory: Int? = null,
    var idAccount: Int? = null,
) : Parcelable