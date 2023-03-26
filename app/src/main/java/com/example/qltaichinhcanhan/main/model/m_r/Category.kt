package com.example.qltaichinhcanhan.main.model.m_r

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

enum class CategoryType {
    EXPENSE,
    INCOME
}

@Parcelize
@Entity(tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "nameCategory")
    var categoryName: String? = null,
    var type: CategoryType? = null,
    var moneyLimit: Float? = null,
    var select: Boolean? = null,
    var icon: Int? = null,
    var color: Int? = null,
    var idAccount: Int? = null,
) : Parcelable
