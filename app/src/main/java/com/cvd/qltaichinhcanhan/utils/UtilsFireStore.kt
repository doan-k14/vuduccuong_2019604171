package com.cvd.qltaichinhcanhan.utils

import android.util.Log
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.m_r.countries
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
        val userAccount = UserAccount(email = email) // Không cần đặt giá trị ID tại đây

        val newAccountUserRef = database.child("user_account").push() // Tạo ID duy nhất

        // Lắng nghe sự kiện hoàn thành việc push để có được ID mới
        newAccountUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newAccountUserId = snapshot.key // Lấy ID mới

                if (newAccountUserId != null) {
                    userAccount.idUserAccount = newAccountUserId // Gán ID mới cho accountUser
                    newAccountUserRef.setValue(userAccount) // Thêm dữ liệu vào cơ sở dữ liệu
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

    interface CBUserAccountLogin {
        fun getSuccess(userAccount: UserAccount)
        fun getFailed()
    }

    private lateinit var cBUserAccountLogin: CBUserAccountLogin

    fun setCBUserAccountLogin(cBUserAccountLogin: CBUserAccountLogin) {
        this.cBUserAccountLogin = cBUserAccountLogin
    }


    fun getUserAccountLogin(email: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("user_account").orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var accountExists = false // Biến để kiểm tra tồn tại của tài khoản
                    var userAccount = UserAccount()
                    for (countrySnapshot in snapshot.children) {
                        accountExists = true
                        userAccount = countrySnapshot.getValue(UserAccount::class.java)!!
                    }

                    if (accountExists) {
                        cBUserAccountLogin.getSuccess(userAccount)
                    } else {
                        cBUserAccountLogin.getFailed()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ")
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
        database.child("money_account").orderByChild("idUserAccount").equalTo(idAccount.toString())
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

    fun setCBListCountry(cBListCountry: CBListCountry) {
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
        val countryList = countries
        val countriesReference = database.child("countries")
        for (country in countryList) {
            countriesReference.child(country.idCountry.toString()).setValue(country)
        }
    }

    fun pushAccountUser(email: String) {
        val database = FirebaseDatabase.getInstance().reference
        val userAccount = UserAccount("f", email)
        database.child("user_account").child(userAccount.idUserAccount.toString())
            .setValue(userAccount)
    }

    // Category
    fun pushListCategory(listCategory: List<Category>) {
        val database = FirebaseDatabase.getInstance().reference
        val categoriesReference = database.child("categories")
        for (category in listCategory) {
            val newCategoryRef = categoriesReference.push()
            val categoryKey = newCategoryRef.key // Lấy ID duy nhất cho mỗi category
            category.idCategory = categoryKey.toString()
            newCategoryRef.setValue(category)
        }
    }
    interface CBListCategory {
        fun getListSuccess(list: List<Category>)
        fun getListFailed()
    }

    private lateinit var cBListCategory: CBListCategory

    fun setCBListCategory(cBListCategory: CBListCategory) {
        this.cBListCategory = cBListCategory
    }

    fun getListCategory(idUserAccount:String,type:Int) {
        val database = FirebaseDatabase.getInstance()
        val countriesReference = database.getReference("categories").orderByChild("idUserAccount").equalTo(idUserAccount)

        countriesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryList = mutableListOf<Category>()

                for (countrySnapshot in snapshot.children) {
                    val category = countrySnapshot.getValue(Category::class.java)
                    if (category != null && category.type == type) {
                        categoryList.add(category)
                    }
                }
                if (categoryList.size != 0) {
                    cBListCategory.getListSuccess(categoryList)
                } else {
                    cBListCategory.getListFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })

    }

    //

    interface CallBackCreateMoneyAccount {
        fun createSuccess(idUserAccount: String)
        fun createFailed()
    }

    private lateinit var callBackCreateMoneyAccount: CallBackCreateMoneyAccount

    fun setCallBackCreateMoneyAccount(callBackCreateMoneyAccount: CallBackCreateMoneyAccount) {
        this.callBackCreateMoneyAccount = callBackCreateMoneyAccount
    }

    fun createMoneyAccount(moneyAccount: MoneyAccount) {
        val database = FirebaseDatabase.getInstance().reference
        val newAccountUserRef = database.child("money_account").push() // Tạo ID duy nhất

        // Lắng nghe sự kiện hoàn thành việc push để có được ID mới
        newAccountUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newAccountUserId = snapshot.key // Lấy ID mới

                if (newAccountUserId != null) {
                    moneyAccount.idMoneyAccount = newAccountUserId // Gán ID mới cho accountUser
                    newAccountUserRef.setValue(moneyAccount) // Thêm dữ liệu vào cơ sở dữ liệu
                    callBackCreateMoneyAccount.createSuccess(newAccountUserId)
                } else {
                    callBackCreateMoneyAccount.createFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }
}