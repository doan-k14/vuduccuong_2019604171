package com.cvd.qltaichinhcanhan.main

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.ActivityNdmainBinding
import com.cvd.qltaichinhcanhan.main.inf.MyCallback
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP

class NDMainActivity : AppCompatActivity(), MyCallback {

    private lateinit var binding: ActivityNdmainBinding
    private lateinit var textNameAccount: TextView
    private lateinit var imageAccount: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNdmainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorStatusbar()

        setSupportActionBar(binding.appBarNdmain.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_ndmain)
        binding.navView.setupWithNavController(navController)

        val headerView = binding.navView.getHeaderView(0)
        textNameAccount = headerView.findViewById<TextView>(R.id.text_name_account)
        imageAccount = headerView.findViewById<ImageView>(R.id.image_account)

        val userAccount = UtilsSharedP.getUserAccountLogin(this)

        textNameAccount.text = userAccount.email
        imageAccount.setImageResource(R.drawable.ic_user_circle)

        textNameAccount.setOnClickListener {
            navController.navigate(R.id.profileFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

    }

    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blu_mani)
        window.navigationBarColor = resources.getColor(R.color.blu_mani)
    }

    override fun onCallback() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onCallbackLockedDrawers() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onCallbackUnLockedDrawers() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onCallbackAccount(accountNew: Account) {
//        account = accountNew
//        if (accountNew.idAccount == 0) {
//            textNameAccount.text = resources.getText(R.string.login)
//        } else {
//            textNameAccount.text = accountNew.accountName
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean("updated_language", false)
        editor.apply()
    }
}