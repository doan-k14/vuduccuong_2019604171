package com.example.qltaichinhcanhan.splash

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityOnBoardingScreenBinding
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.model.m_r.Account
import java.util.*

class OnBoardingScreenActivity : AppCompatActivity(), MyCallback {
    private lateinit var binding: ActivityOnBoardingScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        getCodeLanguage()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setColorStatusbar()
    }

    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blu_mani)
        window.navigationBarColor = resources.getColor(R.color.blu_mani)
    }

    override fun onCallback() {
    }

    override fun onCallbackLockedDrawers() {
    }

    override fun onCallbackUnLockedDrawers() {
    }

    override fun onCallbackAccount(account: Account) {

    }


    fun getCodeLanguage() {
        val preferencesL = PreferenceManager.getDefaultSharedPreferences(this)
        var localeStringL = preferencesL.getBoolean("updated_language", false)
        if(!localeStringL){
            val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
            editor.putBoolean("updated_language", true)
            editor.apply()

            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var localeString = preferences.getString("locale", "")
            if (TextUtils.isEmpty(localeString)) {
                localeString = Locale.getDefault().language
            }
            val configuration = resources.configuration
            configuration.setLocale(Locale(localeString))
            resources.updateConfiguration(configuration, resources.displayMetrics)
            recreate()
        }
    }
}