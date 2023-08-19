package com.cvd.qltaichinhcanhan.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.ActivityOnBoardingScreenBinding
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP

class OnBoardingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        UtilsSharedP.setColorStatusBar(this)
    }
}