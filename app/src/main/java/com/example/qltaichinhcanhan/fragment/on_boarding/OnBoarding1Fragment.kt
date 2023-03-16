package com.example.qltaichinhcanhan.fragment.on_boarding

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val text = getString(R.string.welcome)
        val textWithLineBreaks = text.replace("\\n", "\n")
        val spannableString = SpannableString(textWithLineBreaks)
        val boldSpan = StyleSpan(Typeface.BOLD)
        val colorSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(boldSpan, 23, 49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpan, 23, 49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtTitleMoney.text = spannableString
    }

}