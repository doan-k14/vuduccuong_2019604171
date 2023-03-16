package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentIconCatalogBinding
import com.example.qltaichinhcanhan.main.adapter_main.IconCategoryAdapter
import com.example.qltaichinhcanhan.main.inf.IconClickListener
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.m.IconCategoryData


class IconCatalogFragment : Fragment(),IconClickListener {

    lateinit var binding: FragmentIconCatalogBinding
    private lateinit var iconCategoryAdapter: IconCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentIconCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.rcvIcon.layoutManager = layoutManager
        iconCategoryAdapter = IconCategoryAdapter(requireContext(),IconCategoryData.getIconCategoryList1(),this)
        binding.rcvIcon.adapter = iconCategoryAdapter

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack("C", true)
        }

    }

    override fun onIconClick(icon: Icon) {
        Toast.makeText(context, "Selected icon: ${icon.name}", Toast.LENGTH_SHORT).show()
    }


}