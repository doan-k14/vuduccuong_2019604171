package com.cvd.qltaichinhcanhan.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.google.gson.Gson
import java.util.regex.Matcher
import java.util.regex.Pattern

object UtilsSharedP {
    private const val KEY_PREF = "CVD_2023"

    private fun getSharedPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
    }

    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sharedPreferences: SharedPreferences = context?.let { getSharedPreference(it) }!!
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String?): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            KEY_PREF,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(context: Context?, key: String?, value: String?) {
        val sharedPreferences: SharedPreferences = context?.let { getSharedPreference(it) }!!
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(context: Context?, key: String?): String? {
        val sharedPreferences: SharedPreferences = context?.let { getSharedPreference(it) }!!
        return sharedPreferences.getString(key, "")
    }

    fun setColorStatusBar(activity:Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.blu_mani)
        window.navigationBarColor = activity.resources.getColor(R.color.blu_mani)
    }

    fun isValidGmail(email: String?): Boolean {
        val gmailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$"
        val pattern: Pattern = Pattern.compile(gmailPattern)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun saveUserAccountLogin(context: Context,userAccount: UserAccount){
        val gson = Gson()
        val stringUserAccount = gson.toJson(userAccount)
        putString(context,Constant.USER_LOGIN_SUCCESS,stringUserAccount)
    }

    fun getUserAccountLogin(context: Context): UserAccount {
        val stringUserAccount = getString(context, Constant.USER_LOGIN_SUCCESS)
        val gson = Gson()
        return gson.fromJson(stringUserAccount, UserAccount::class.java)
    }

    fun getCountryDefault(context: Context): Country {
        val stringCountryDefault = getString(context,Constant.COUNTRY_DEFAULT)
        val gson = Gson()
        return gson.fromJson(stringCountryDefault,Country::class.java)?: Country()
    }
    fun saveCountryDefault(context: Context, country: Country) {
        val gson = Gson()
        val stringAccount = gson.toJson(country)
        putString(context,Constant.COUNTRY_DEFAULT,stringAccount)
    }
}