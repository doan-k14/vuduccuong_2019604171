package com.example.qltaichinhcanhan.main

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityNdmainBinding
import com.example.qltaichinhcanhan.main.inf.MyCallback

class NDMainActivity : AppCompatActivity(), MyCallback {

    private lateinit var binding: ActivityNdmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNdmainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorStatusbar()

        setSupportActionBar(binding.appBarNdmain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_ndmain)
        binding.navView.setupWithNavController(navController)


        binding.appBarNdmain.btnDialog.setOnClickListener {
            Toast.makeText(this, "dialog", Toast.LENGTH_SHORT).show()
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

    override fun onCallbackCategoryToEditC() {
//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ndmain)
//        navController.navigate(R.id.action_nav_home_to_editCategoryFragment)
    }
}