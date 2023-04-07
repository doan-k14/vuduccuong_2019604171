package com.example.qltaichinhcanhan.main.model.m

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
//    @SerializedName("idAccount")
//    val idAccount: Int = 0,
//    var accountName: String? = null,
//    var password: String? = null,
//    var urlImage: String? = null,
//    var selectAccount: Boolean? = null,
//)
//@Entity(tableName = "country")
//data class Country(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("idCountry")
//    val idCountry: Int = 0,
//    var countryName: String? = null,
//    var currencyCode: String? = null,
//    var currencyName: String? = null,
//    var currencySymbol: String? = null,
//    var flagUrl: String? = null,
//    var exchangeRate: Float? = null,
//    var selectCountry: Boolean? = null,
//)
//
//@Parcelize
//@Entity(tableName = "moneyAccount",
//    foreignKeys = [
//        ForeignKey(
//            entity = Country::class,
//            parentColumns = ["idCountry"],
//            childColumns = ["idCountry"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = Account::class,
//            parentColumns = ["idAccount"],
//            childColumns = ["idAccount"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class MoneyAccount(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("idMoneyAccount")
//    var idMoneyAccount: Int = 0,
//    var moneyAccountName: String? = null,
//    var amountMoneyAccount: Float? = null,
//    var selectMoneyAccount: Boolean? = null,
//    var icon: Int? = null,
//    var color: Int? = null,
//    var idCountry: Int? = null,
//    var idAccount: Int? = null,
//) : Parcelable
//
//@Parcelize
//@Entity(tableName = "transaction",
//    foreignKeys = [
//        ForeignKey(
//            entity = MoneyAccount::class,
//            parentColumns = ["idMoneyAccount"],
//            childColumns = ["idMoneyAccount"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = Category::class,
//            parentColumns = ["idCategory"],
//            childColumns = ["idCategory"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = Account::class,
//            parentColumns = ["idAccount"],
//            childColumns = ["idAccount"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//
//data class Transaction(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("idTransaction")
//    var idTransaction: Int = 0,
//    var transactionName: String? = null,
//    var transactionAmount: Float? = null,
//    var day: Long? = null,
//    var comment: String? = null,
//    var selectTransaction: Boolean? = null,
//    var idMoneyAccount: Int? = null,
//    var idCategory: Int? = null,
//    var idAccount: Int? = null,
//) : Parcelable
//@Parcelize
//@Entity(tableName = "category",
//    foreignKeys = [
//        ForeignKey(
//            entity = Account::class,
//            parentColumns = ["idAccount"],
//            childColumns = ["idAccount"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class Category(
//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("idCategory")
//    var idCategory: Int = 0,
//    var categoryName: String? = null,
//    var type: CategoryType? = null,
//    var moneyLimit: Float? = null,
//    var selectCategory: Boolean? = null,
//    var icon: Int? = null,
//    var color: Int? = null,
//    var idAccount: Int? = null,
//) : Parcelable