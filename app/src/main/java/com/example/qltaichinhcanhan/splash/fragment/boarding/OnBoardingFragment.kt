package com.example.qltaichinhcanhan.splash.fragment.boarding

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.OnBoardingPagerAdapter
import com.example.qltaichinhcanhan.databinding.FragmentOnBoardingBinding
import java.util.*


class OnBoardingFragment : Fragment() {
    lateinit var binding: FragmentOnBoardingBinding
    private lateinit var onBoardingPagerAdapter: OnBoardingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBoardingPagerAdapter = OnBoardingPagerAdapter(requireActivity())
        binding.viewPagerLogin.adapter = onBoardingPagerAdapter
        binding.indicator.setViewPager(binding.viewPagerLogin)

        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_signUpFragment)
        }
        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
        binding.textNewStart.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_creatsMoneyFragment)
        }
        binding.textLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_languageFragment2)
        }
    }
}