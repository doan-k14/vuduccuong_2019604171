package com.example.qltaichinhcanhan.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityOnBoardingScreenBinding
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.m.Icon
import kotlinx.coroutines.*

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