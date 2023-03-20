package com.example.qltaichinhcanhan.fragment.on_boarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityOnBoardingScreenBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.m.Country
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.retrofit.CountryService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnBoardingScreenActivity : AppCompatActivity(), MyCallback {
    private lateinit var binding: ActivityOnBoardingScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setContentView(binding.root)
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

    override fun onCallbackCategoryToEditC() {
    }

    override fun onIconClick(icon: Icon) {
    }
}