package com.example.qltaichinhcanhan.mode

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Parcelize
@Entity(tableName = "Money")
data class Money(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type: Int? = null, // loại e = 1, in =2
    var day: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var currency: Int? = null, // đơn vị tiền
    var amount: Int? = null,    // số lượng tiền
    var note: String? = null,
    var category: Int? = null, // loại
): Parcelable, Serializable

