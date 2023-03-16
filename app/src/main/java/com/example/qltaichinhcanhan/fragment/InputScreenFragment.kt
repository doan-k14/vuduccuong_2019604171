package com.example.qltaichinhcanhan.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentInputScreenBinding


class InputScreenFragment : Fragment() {
    private lateinit var binding: FragmentInputScreenBinding

    fun newInstance(): InputScreenFragment {
        val args = Bundle()
        val fragment = InputScreenFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initView()

    }

    private fun initView() {

        binding.btnExpense.isActivated = true
        binding.btnIncome.isActivated = false
        createChildFragment(listFragment[0])

        binding.btnExpense.setOnClickListener {
            binding.btnExpense.isActivated = true
            binding.btnIncome.isActivated = false
            createChildFragment(listFragment[0])
        }

        binding.btnIncome.setOnClickListener {
            binding.btnExpense.isActivated = false
            binding.btnIncome.isActivated = true
            createChildFragment(listFragment[1])
        }
    }

    private fun createChildFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout_input_screen, fragment).commit()
    }

    private val listFragment = listOf(
        ExpenseFragment().newInstance(),
        InComeFragment().newInstance()
    )
}