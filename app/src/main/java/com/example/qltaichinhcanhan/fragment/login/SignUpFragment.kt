package com.example.qltaichinhcanhan.fragment.login

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentLoginBinding
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