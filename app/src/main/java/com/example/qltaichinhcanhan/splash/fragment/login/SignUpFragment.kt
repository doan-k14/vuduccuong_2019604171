package com.example.qltaichinhcanhan.splash.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentSignupBinding


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.imgTest.setOnClickListener {
//            binding.imgTest.setBackgroundResource(R.drawable.color_icon_yellow)
//
////            val color = ContextCompat.getColor(requireContext(), R.color.blue)
////            binding.imgTest.setBackgroundColor(Color.parseColor("#FFA500"))
////            binding.imgTest.setBackgroundColor(color)
//        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_creatsMoneyFragment)
        }

        binding.txtLoginNow.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }


    }

}