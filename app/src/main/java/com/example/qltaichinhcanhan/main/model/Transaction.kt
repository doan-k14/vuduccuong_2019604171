package com.example.qltaichinhcanhan.main.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transaction",
    foreignKeys = [
        ForeignKey(entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["idCategory1"],
            onDelete = ForeignKey.CASCADE)
    ])

data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    var nameTransaction: String? = null,
    var amountTransaction: Float? = null,
    var day: Long? = null,
    var comment: String? = null,
    var idAccount: Int? = null,
    var idCategory1: Int? = null,
): Parcelable