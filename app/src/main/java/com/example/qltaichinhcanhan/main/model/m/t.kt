package com.example.qltaichinhcanhan.main.model.m
//
//import android.os.Parcelable
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.PrimaryKey
//import com.example.qltaichinhcanhan.main.model.m_r.Account
//import com.example.qltaichinhcanhan.main.model.m_r.Country
//import com.google.gson.annotations.SerializedName
//import kotlinx.parcelize.Parcelize
////
//@Entity(tableName = "account")
//data class Account(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("id")
//    val id: Int = 0,
//    var accountName: String? = null,
//    var password: String? = null,
//    var urlImage: String? = null,
//    var select: Boolean? = null,
//)
//
//@Entity(tableName = "country")
//data class Country(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("id")
//    val id: Int = 0,
//    var countryName: String? = null,
//    var currencyCode: String? = null,
//    var currencyName: String? = null,
//    var currencySymbol: String? = null,
//    var flagUrl: String? = null,
//    var exchangeRate: Float? = null,
//    var select: Boolean? = null,
//)
//
//@Parcelize
//@Entity(tableName = "moneyAccount",
//    foreignKeys = [
//        ForeignKey(
//            entity = Country::class,
//            parentColumns = ["id"],
//            childColumns = ["idCountry"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = Account::class,
//            parentColumns = ["id"],
//            childColumns = ["idAccount"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class MoneyAccount(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("id")
//    val id: Int = 0,
//    @ColumnInfo(name = "moneyAccountName")
//    var moneyAccountName: String? = null,
//    var amountMoneyAccount: Float? = null,
//    var select: Boolean? = null,
//    var icon: Int? = null,
//    var color: Int? = null,
//    var idCountry: Int? = null,
//    var idAccount: Int? = null,
//) : Parcelable
