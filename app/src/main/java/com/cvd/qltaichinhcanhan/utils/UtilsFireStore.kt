package com.cvd.qltaichinhcanhan.utils

import android.util.Log
import com.cvd.qltaichinhcanhan.main.model.m_new.*
import com.cvd.qltaichinhcanhan.main.model.m_r.countryList
import com.google.firebase.database.*
import com.google.gson.Gson

class UtilsFireStore {
    val TAG: String = "UtilsFireStore"
    // user account

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
        val userAccount = UserAccount(email = email, countryDefault = Country()) // Không cần đặt giá trị ID tại đây

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

    interface CBUpdateUserAccount {
        fun updateSuccess()
        fun updateFailed()
    }

    private lateinit var cBUpdateUserAccount: CBUpdateUserAccount

    fun setCBUpdateUserAccount(cBUpdateUserAccount: CBUpdateUserAccount) {
        this.cBUpdateUserAccount = cBUpdateUserAccount
    }

    fun updateUserAccount(idUserAccount: String,userAccount: UserAccount) {
        val database = FirebaseDatabase.getInstance().reference
        val moneyAccountReference = database.child("user_account").child(idUserAccount)

        moneyAccountReference.setValue(userAccount)
            .addOnSuccessListener {
                cBUpdateUserAccount.updateSuccess()
            }
            .addOnFailureListener {
                cBUpdateUserAccount.updateFailed()
            }
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
                        val idUserAccount = countrySnapshot.child("idUserAccount").getValue(String::class.java)
                        val email = countrySnapshot.child("email").getValue(String::class.java)
                        val country = countrySnapshot.child("countryDefault").getValue(Country::class.java)

                        if (idUserAccount != null && email != null) {
                            userAccount.idUserAccount = idUserAccount
                            userAccount.email = email
                            userAccount.countryDefault = country
                            accountExists = true
                        }
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
                    var country = Country()
                    for (countrySnapshot in snapshot.children) {
                        val selectMoneyAccount = countrySnapshot.child("selectMoneyAccount").getValue(Boolean::class.java)
                        if(selectMoneyAccount == true){
                            accountExists = true
                            country = countrySnapshot.child("country").getValue(Country::class.java)!!
                        }
                    }

                    if (accountExists) {
                        val gson = Gson()
                        val stringCountry = gson.toJson(country)
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




   fun setStatusUpdateListCountry(checkUpdate: Boolean){
       val database = FirebaseDatabase.getInstance()
       val countriesReference = database.getReference("check_update_country")
       countriesReference.setValue(checkUpdate)
   }


    fun getStatusUpdateListCountry(checkUpdate: Boolean){
        val database = FirebaseDatabase.getInstance()
        val checkUpdateReference = database.getReference("check_update")

        checkUpdateReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val checkUpdate = snapshot.getValue(Boolean::class.java)
                if (checkUpdate != null) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }
    // lúc tạo moneyaccount thì lấy luôn list đã conver, list conver lấy từ checkUpdate true
    // lúc tạo moneyaccount thì lấy luôn list đã conver, list conver lấy từ checkUpdate true



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
        val countryList = countryList
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
    interface CBUpdateMoneyAccount {
        fun updateSuccess()
        fun updateFailed()
    }

    private lateinit var cBUpdateMoneyAccount: CBUpdateMoneyAccount

    fun setCBUpdateMoneyAccount(cBUpdateMoneyAccount: CBUpdateMoneyAccount) {
        this.cBUpdateMoneyAccount = cBUpdateMoneyAccount
    }

    fun updateMoneyAccountById(idCategory: String,updatedMoneyAccount: MoneyAccount){
        val database = FirebaseDatabase.getInstance().reference
        val moneyAccountReference = database.child("money_account").child(idCategory)

        moneyAccountReference.setValue(updatedMoneyAccount)
            .addOnSuccessListener {
                cBUpdateMoneyAccount.updateSuccess()
            }
            .addOnFailureListener {
                cBUpdateMoneyAccount.updateFailed()
            }
    }
    interface CBUpdateListMoneyAccount {
        fun updateSuccess()
        fun updateFailed()
    }

    private lateinit var cBUpdateListMoneyAccount: CBUpdateListMoneyAccount

    fun setCBUpdateListMoneyAccount(cBUpdateListMoneyAccount: CBUpdateListMoneyAccount) {
        this.cBUpdateListMoneyAccount = cBUpdateListMoneyAccount
    }

    fun updateMoneyAccountsOnFirebase(moneyAccountList: List<MoneyAccount>) {
        val database = FirebaseDatabase.getInstance()
        val moneyAccountsReference = database.getReference("money_account")

        for (moneyAccount in moneyAccountList) {
            val moneyAccountReference = moneyAccountsReference.child(moneyAccount.idMoneyAccount ?: "")
            moneyAccountReference.setValue(moneyAccount) { databaseError, databaseReference ->
                if (databaseError != null) {
                    cBUpdateListMoneyAccount.updateFailed()

                } else {
                    cBUpdateListMoneyAccount.updateSuccess()
                }
            }
        }
    }

    interface CBDeleteMoneyAccount {
        fun deleteSuccess()
        fun deleteFailed()
    }

    private lateinit var cBDeleteMoneyAccount: CBDeleteMoneyAccount

    fun setCBDeleteMoneyAccount(cBDeleteMoneyAccount: CBDeleteMoneyAccount) {
        this.cBDeleteMoneyAccount = cBDeleteMoneyAccount
    }

    fun deleteMoneyAccountById(id: String){
        val database = FirebaseDatabase.getInstance().reference
        val categoryReference = database.child("money_account").child(id)

        categoryReference.removeValue()
            .addOnSuccessListener {
                cBDeleteMoneyAccount.deleteSuccess()
            }
            .addOnFailureListener {
                cBDeleteMoneyAccount.deleteFailed()
            }
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

                    if (idMoneyAccount != null && moneyAccountName != null && amountMoneyAccount != null && idUserAccount != null && icon != null && country != null) {

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