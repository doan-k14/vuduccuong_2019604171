package com.example.qltaichinhcanhan

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qltaichinhcanhan.databinding.ActivitySplashBinding
import com.example.qltaichinhcanhan.fragment.login.LoginFragment

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorStatusbar()
        switchToFragment(LoginFragment())

//        val sharedPreferences: SharedPreferences =
//            getSharedPreferences("splash", Context.MODE_PRIVATE)
//        val checkData = sharedPreferences.getBoolean("dataSplash", false)
//        if (!checkData) {
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("dataSplash", true)
//            editor.commit()
//            val mainHandler = Handler(Looper.getMainLooper()).postDelayed({
//                switchToFragment(CreatsMoneyFragment())
//            }, 3000)
//        } else {
//            lifecycleScope.launch {
//                delay(2000)
//                withContext(Dispatchers.Main) {
//                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
    }

    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        window.navigationBarColor = resources.getColor(R.color.main_color)
    }

    private fun switchToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fl_splash, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

}