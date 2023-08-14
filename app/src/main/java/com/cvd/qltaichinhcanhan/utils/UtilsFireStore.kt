package com.cvd.qltaichinhcanhan.utils

import android.util.Log
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.m_r.countries
import com.google.firebase.database.*
import com.google.gson.Gson

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

    fun createTest(){
        val userAccount = UserAccount("adsdasdas",email = "dsadsadasd") // Không cần đặt giá trị ID tại đây
        val list = listOf<UserAccount>(UserAccount("dđ","dsdsd"), UserAccount("ssss","ssss"))
        val database = FirebaseDatabase.getInstance().reference
        database.child("node 1").child("mode 1_1").child("mode 1_1_1").setValue(userAccount)
        database.child("node 1").child("mode 1_1").setValue(list)
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
//                        val country = countrySnapshot.getValue(MoneyAccount::class.java)
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

    // create 1 category
    interface CBCreateCategory{
        fun createSuccess()
        fun createFailed()
    }

    private lateinit var cbCreateCategory:CBCreateCategory

    fun setCBCreateCategory(cbCreateCategory: CBCreateCategory) {
        this.cbCreateCategory = cbCreateCategory
    }

    fun createCategory(category: Category){
        val database = FirebaseDatabase.getInstance().reference
        val newCategoryReference = database.child("categories").push()
        newCategoryReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val newId = snapshot.key
                if(newId != null){
                    category.idCategory = newId
                    newCategoryReference.setValue(category)
                    cbCreateCategory.createSuccess()
                }else{
                    cbCreateCategory.createFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    interface CBDeleteCategory {
        fun deleteSuccess()
        fun deleteFailed()
    }

    private lateinit var cBDeleteCategory: CBDeleteCategory

    fun setCBDeleteCategory(cBDeleteCategory: CBDeleteCategory) {
        this.cBDeleteCategory = cBDeleteCategory
    }

    fun deleteCategoryById(idCategory: String){
        val database = FirebaseDatabase.getInstance().reference
        val categoryReference = database.child("categories").child(idCategory)

        categoryReference.removeValue()
            .addOnSuccessListener {
                cBDeleteCategory.deleteSuccess()
            }
            .addOnFailureListener {
                cBDeleteCategory.deleteFailed()
            }
    }
    interface CBUpdateCategory {
        fun updateSuccess()
        fun updateFailed()
    }

    private lateinit var cBUpdateCategory: CBUpdateCategory

    fun setCBUpdateCategory(cBUpdateCategory: CBUpdateCategory) {
        this.cBUpdateCategory = cBUpdateCategory
    }

    fun updateCategoryById(idCategory: String,updatedCategory: Category){
        val database = FirebaseDatabase.getInstance().reference
        val categoryReference = database.child("categories").child(idCategory)

        categoryReference.setValue(updatedCategory)
            .addOnSuccessListener {
                cBUpdateCategory.updateSuccess()
            }
            .addOnFailureListener {
                cBUpdateCategory.updateFailed()
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

    // money account

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

    interface CBListMoneyAccount {
        fun getListSuccess(list: List<MoneyAccount>)
        fun getListFailed()
    }

    private lateinit var cBListMoneyAccount: CBListMoneyAccount

    fun setCBListMoneyAccount(cBListMoneyAccount: CBListMoneyAccount) {
        this.cBListMoneyAccount = cBListMoneyAccount
    }

    fun getListMoneyAccount(idUserAccount:String) {
        val database = FirebaseDatabase.getInstance()
        val countriesReference = database.getReference("money_account").orderByChild("idUserAccount").equalTo(idUserAccount)

        countriesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moneyAccountList = mutableListOf<MoneyAccount>()

                for (countrySnapshot in snapshot.children) {

                    val idMoneyAccount = countrySnapshot.child("idMoneyAccount").getValue(String::class.java)
                    val moneyAccountName = countrySnapshot.child("moneyAccountName").getValue(String::class.java)
                    val amountMoneyAccount = countrySnapshot.child("amountMoneyAccount").getValue(Float::class.java)
                    val selectMoneyAccount = countrySnapshot.child("selectMoneyAccount").getValue(Boolean::class.java)
                    val idUserAccount = countrySnapshot.child("idUserAccount").getValue(String::class.java)
                    val icon = countrySnapshot.child("icon").getValue(IConVD::class.java)
                    val country = countrySnapshot.child("country").getValue(Country::class.java)

                    if (idMoneyAccount != null && moneyAccountName != null && amountMoneyAccount != null &&
                        selectMoneyAccount != null && idUserAccount != null && icon != null && country != null) {

                        val moneyAccount = MoneyAccount(
                            idMoneyAccount,
                            moneyAccountName,
                            amountMoneyAccount,
                            selectMoneyAccount,
                            idUserAccount,
                            icon,
                            country
                        )

                        moneyAccountList.add(moneyAccount)
                    }
                }

                if (moneyAccountList.size != 0) {
                    cBListMoneyAccount.getListSuccess(moneyAccountList)
                } else {
                    cBListMoneyAccount.getListFailed()
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })

    }
}