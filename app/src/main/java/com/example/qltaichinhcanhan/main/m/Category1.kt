package com.example.qltaichinhcanhan.main.m

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category1")
data class Category1(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "nameCategory")
    var nameCategory: String? = null,
    var type: Int? = null, // 1: expense 2: income
    var lave: Float? = null,
    var icon: String? = null,
    var color: Int? = null,
    var select: Boolean? = null,
) : Parcelable
