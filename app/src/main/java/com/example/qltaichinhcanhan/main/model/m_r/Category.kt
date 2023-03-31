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
            parentColumns = ["idAccount"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idCategory")
    var idCategory: Int = 0,
    var categoryName: String? = null,
    var type: CategoryType? = null,
    var moneyLimit: Float? = null,
    var selectCategory: Boolean? = null,
    var icon: Int? = null,
    var color: Int? = null,
    var idAccount: Int? = null,
) : Parcelable
