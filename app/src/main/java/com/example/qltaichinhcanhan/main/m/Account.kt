package com.example.qltaichinhcanhan.main.m

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*
@Parcelize
@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @ColumnInfo(name = "nameAccount")
    var nameAccount: String? = null,
    var typeMoney: Int? = null, // 1: VND, USD
    var amountAccount: Float? = null,
    var icon: Int? = null,
    var color: Int? = null,
    var select: Boolean? = null,
): Parcelable

