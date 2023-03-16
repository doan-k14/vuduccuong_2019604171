package com.example.qltaichinhcanhan.mode

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int,
    var name: String? = null,
    var type: Int? = null, // 1: expense 2: income
    var select: Boolean? = null,
) : Parcelable