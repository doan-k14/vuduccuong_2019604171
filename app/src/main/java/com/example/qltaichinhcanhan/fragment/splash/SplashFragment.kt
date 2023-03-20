package com.example.qltaichinhcanhan.fragment.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.m.Country
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.retrofit.CountryService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SplashFragment : Fragment() {
    lateinit var countryViewMode: CountryViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryViewMode = ViewModelProvider(this)[CountryViewMode::class.java]


        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("default_account_initialization_check",
                Context.MODE_PRIVATE)
        val checkData = sharedPreferences.getBoolean("ck", false)

        if (!checkData) {
            lifecycleScope.launch {
                delay(1000)
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }

        } else {
            lifecycleScope.launch {
                delay(1000)
                val intent = Intent(requireActivity(), NDMainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}