package com.cvd.qltaichinhcanhan.utils

import android.content.Context
import android.content.SharedPreferences

object Utils {
    private const val KEY_PREF = "CVD_2023"

    private fun getSharedPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
    }

    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String?, defaultValue: Boolean): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            KEY_PREF,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putString(context: Context?, key: String?, value: String?) {
        val sharedPreferences: SharedPreferences = context?.let { getSharedPreference(it) }!!
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(context: Context?, key: String?): String? {
        val sharedPreferences: SharedPreferences = context?.let { getSharedPreference(it) }!!
        return sharedPreferences.getString(key, "")
    }

}