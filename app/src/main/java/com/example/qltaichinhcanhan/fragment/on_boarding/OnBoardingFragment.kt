package com.example.qltaichinhcanhan.fragment.on_boarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.OnBoardingPagerAdapter
import com.example.qltaichinhcanhan.databinding.FragmentAccountsBinding
import com.example.qltaichinhcanhan.databinding.FragmentOnBoardingBinding
import com.example.qltaichinhcanhan.fragment.login.LoginFragment
import com.example.qltaichinhcanhan.fragment.login.SignUpFragment


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
    }

}