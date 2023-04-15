package com.example.qltaichinhcanhan.splash.fragment.boarding

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentOnBoarding1Binding


class OnBoarding1Fragment : Fragment() {

    private lateinit var binding: FragmentOnBoarding1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnBoarding1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val t = requireContext().resources.getString(R.string.personal_financial_management_application)
        val welcomeTo = requireContext().resources.getString(R.string.welcome_to)
        val startManaging = requireContext().resources.getString(R.string.register_now)
        val textMessage = "$welcomeTo $t $startManaging"

        val spannableString = SpannableString(textMessage)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)),
            welcomeTo.length,
            welcomeTo.length + t.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(StyleSpan(Typeface.BOLD),
            welcomeTo.length,
            welcomeTo.length + t.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtTitleMoney.text = spannableString
    }

}