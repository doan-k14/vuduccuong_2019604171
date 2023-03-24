package com.example.qltaichinhcanhan.main.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @ColumnInfo(name = "nameAccount")
    var nameAccount: String? = null,
    var typeMoney: String? = null, // 1: VND, USD
    var amountAccount: Float? = null,
    var icon: String? = null,
    var color: Int? = null,
    var select: Boolean? = null,
): Parcelable

