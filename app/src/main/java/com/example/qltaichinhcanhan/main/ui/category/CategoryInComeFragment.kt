package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentAccountsBinding
import com.example.qltaichinhcanhan.databinding.FragmentCategoryBinding
import com.example.qltaichinhcanhan.databinding.FragmentCategoryInComeBinding


class CategoryInComeFragment : Fragment() {

    lateinit var binding: FragmentCategoryInComeBinding

    fun newInstance(): CategoryInComeFragment {
        val args = Bundle()
        val fragment = CategoryInComeFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryInComeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}