package com.cvd.qltaichinhcanhan.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.cvd.qltaichinhcanhan.R

object Utils {
    private const val KEY_PREF = "CVD_2023"

    private fun getSharedPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
    }

    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
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
}