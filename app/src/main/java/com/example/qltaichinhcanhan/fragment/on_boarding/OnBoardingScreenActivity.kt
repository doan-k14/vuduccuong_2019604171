package com.example.qltaichinhcanhan.fragment.on_boarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityOnBoardingScreenBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnBoardingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorStatusbar()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

//        val sharedPreferences: SharedPreferences =
//            getSharedPreferences("splash", Context.MODE_PRIVATE)
//        val checkData = sharedPreferences.getBoolean("dataSplash", false)
//
//        if (!checkData) {
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("dataSplash", true)
//            editor.commit()
//            val mainHandler = Handler(Looper.getMainLooper()).postDelayed({
//                navController.navigate(R.id.action_splashFragment_to_onBoardingFragment)
//            }, 3000)
//
//        } else {
//            lifecycleScope.launch {
//                delay(2000)
//                withContext(Dispatchers.Main) {
//                    val intent = Intent(this@OnBoardingScreenActivity, NDMainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }

//        navController.popBackStack()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }


    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blu_mani)
        window.navigationBarColor = resources.getColor(R.color.blu_mani)
    }

    private fun switchToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.ll_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}