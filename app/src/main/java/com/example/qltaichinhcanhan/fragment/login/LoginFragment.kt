package com.example.qltaichinhcanhan.fragment.login

import android.graphics.Color
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtPass.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        val transformationMethod = PasswordTransformationMethod()

        binding.edtPass.transformationMethod = transformationMethod

        binding.imgEyePass.setOnClickListener {
            if (binding.edtPass.transformationMethod == transformationMethod) {
                binding.edtPass.transformationMethod = null
                binding.imgEyePass.isActivated = false

            } else {
                binding.edtPass.transformationMethod = transformationMethod
                binding.imgEyePass.isActivated = true
            }
        }

        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_creatsMoneyFragment)
        }

    }

}