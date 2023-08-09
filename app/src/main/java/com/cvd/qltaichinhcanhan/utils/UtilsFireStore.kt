package com.cvd.qltaichinhcanhan.utils

import android.content.Context
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.model.AccountUser
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UtilsFireStore {
    val TAG: String = "UtilsFireStore"

    interface CallBackCreateAccountUser {
        fun createSuccess(idUserAccount: String)
        fun createFailed()
    }

    private lateinit var callBackCreateAccountUser: CallBackCreateAccountUser

    fun setCallBackCreateAccountUser(callBackCreateAccountUser: CallBackCreateAccountUser) {
        this.callBackCreateAccountUser = callBackCreateAccountUser
    }

    fun createUserAccount(email: String) {
        val database = FirebaseDatabase.getInstance().reference
        val accountUser = AccountUser(emailName = email) // Không cần đặt giá trị ID tại đây

        val newAccountUserRef = database.child("user_account").push() // Tạo ID duy nhất

        // Lắng nghe sự kiện hoàn thành việc push để có được ID mới
        newAccountUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newAccountUserId = snapshot.key // Lấy ID mới

                if (newAccountUserId != null) {
                    accountUser.idAccount = newAccountUserId // Gán ID mới cho accountUser
                    newAccountUserRef.setValue(accountUser) // Thêm dữ liệu vào cơ sở dữ liệu
                    callBackCreateAccountUser.createSuccess(newAccountUserId)
                } else {
                    callBackCreateAccountUser.createFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    interface CBAccountMoneyByEmail {
        fun getSuccess()
        fun getFailed()
    }

    private lateinit var cBAccountMoneyByEmail: CBAccountMoneyByEmail

    fun setCBAccountMoneyByEmail(cBAccountMoneyByEmail: CBAccountMoneyByEmail) {
        this.cBAccountMoneyByEmail = cBAccountMoneyByEmail
    }


    fun getAccountMoneyByEmail(idAccount: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("money_account").orderByChild("idAccount").equalTo(idAccount.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var accountExists = false // Biến để kiểm tra tồn tại của tài khoản

                    for (countrySnapshot in snapshot.children) {
                        accountExists = true
                        val country = countrySnapshot.getValue(MoneyAccount::class.java)
                    }

                    if (accountExists) {
                        cBAccountMoneyByEmail.getSuccess()
                    } else {
                        cBAccountMoneyByEmail.getFailed()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ")
                }
            })
    }


    interface CBListCountry {
        fun getListSuccess(list: List<Country>)
        fun getListFailed()
    }

    private lateinit var cBListCountry: CBListCountry

    fun setCBListCountry(cBAccountMoneyByEmail: CBListCountry) {
        this.cBListCountry = cBListCountry
    }

    fun getListCountry() {
        val database = FirebaseDatabase.getInstance()
        val countriesReference = database.getReference("countries")

        countriesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val countryList = mutableListOf<Country>()

                for (countrySnapshot in snapshot.children) {
                    val country = countrySnapshot.getValue(Country::class.java)
                    if (country != null) {
                        countryList.add(country)
                    }
                }
                if (countryList.size != 0) {
                    cBListCountry.getListSuccess(countryList)
                } else {
                    cBListCountry.getListFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })

    }

    fun pushListCountry() {
        val database = FirebaseDatabase.getInstance().reference
        val countryList = mutableListOf(
            Country(idCountry = 1, countryName = "Vietnam"),
            Country(idCountry = 2, countryName = "USA"),
            Country(idCountry = 3, countryName = "USA3"),
            Country(idCountry = 4, countryName = "USA4"),
            Country(idCountry = 5, countryName = "USA5"),
        )

        val countriesReference = database.child("countries")
        for (country in countryList) {
            countriesReference.child(country.idCountry.toString()).setValue(country)
        }
    }

    fun pushAccountUser(email: String) {
        val database = FirebaseDatabase.getInstance().reference
        val accountUser = AccountUser("f", email)
        database.child("user_account").child(accountUser.idAccount.toString()).setValue(accountUser)
    }
}