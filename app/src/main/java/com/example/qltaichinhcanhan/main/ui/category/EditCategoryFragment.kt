package com.example.qltaichinhcanhan.main.ui.category

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.m.IconCategoryData
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.example.qltaichinhcanhan.mode.Category


class EditCategoryFragment : Fragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var categoryViewModel: CategoryViewMode
    var category1 = Category1()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        val selectedCategory = categoryViewModel.category
        // checklaij vi dang co 2 id = 1,2
        if (selectedCategory.id != 2) {
            binding.imgIconCategory.setImageResource(IconCategoryData.showICon(requireContext(),
                selectedCategory.icon!!))
            binding.edtNameCategory.setText(selectedCategory.nameCategory)
            binding.edtPlannedOutlay.setText(selectedCategory.lave.toString())
            categoryViewModel.icon.name = selectedCategory.icon!!
            binding.llUpdateCategory.visibility = View.VISIBLE
            binding.txtTypeCategory.visibility = View.VISIBLE
            binding.textCreateCategory.visibility = View.GONE
            binding.llTypeCategory.visibility = View.GONE
        } else {
            binding.textTitleTotal.setText(R.string.create_category)
            binding.llUpdateCategory.visibility = View.GONE
            binding.txtTypeCategory.visibility = View.GONE
            binding.textCreateCategory.visibility = View.VISIBLE
            binding.llTypeCategory.visibility = View.VISIBLE
        }


        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        val list = DataColor.listCategory

        adapterIconCategory = AdapterIconCategory(requireContext(),
            list,
            AdapterIconCategory.LayoutType.TYPE2)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1


        adapterIconCategory.setClickItemSelect {
            if (it.id == 1) {
                findNavController().navigate(R.id.action_editCategoryFragment_to_iconCatalogFragment)
            } else {
                binding.imgIconCategory.setImageResource(DataColor.showBackgroundColorCircle(
                    requireContext(),
                    it.icon!!))
                categoryViewModel.icon.name = it.icon!!
            }
        }


        adapterIConColor =
            AdapterIConColor(requireContext(), DataColor.listImageCheckCircle)
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapterIConColor.setClickItemSelect {
            val id = DataColor.getIdColorById(it.idColor)
            binding.imgIconCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(
                requireContext(),
                id!!))
            adapterIconCategory.updateColor(it.idColor)
            categoryViewModel.icon.color = it.idColor
        }

        binding.textSaveCategory.setOnClickListener {
            if (checkData("default")) {
                categoryViewModel.updateCategory(category1)
                findNavController().popBackStack()
                clearData()
            }
        }

        binding.textCreateCategory.setOnClickListener {
            if (checkData("Thêm")) {
                categoryViewModel.addCategory(category1)
                findNavController().popBackStack()
                clearData()
            }
        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, category1)
        }


    }

    private fun clearData() {
        categoryViewModel.category = Category1()
        categoryViewModel.icon = Icon(0, "default", 0, 0)
    }

    private fun checkData(name: String): Boolean {
        category1 = categoryViewModel.category
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),
                "Tên danh mục không được bỏ trống!",
                Toast.LENGTH_SHORT).show()
            return false
        }
        category1.nameCategory = textName.toString()

        var textPlannedOutlay = binding.edtPlannedOutlay.text

        if (categoryViewModel.icon.name == name) {
            Toast.makeText(requireContext(),
                "Tên danh mục không được bỏ trống!",
                Toast.LENGTH_SHORT).show()
            return false
        }
        category1.icon = categoryViewModel.icon.name
        category1.color = categoryViewModel.icon.color
        // sau khi lưu hoặc xóa thì clear data categpory, icon
        return true
    }


    private fun createDialogDelete(gravity: Int, category: Category1) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        wLayoutParams.gravity = gravity
        window.attributes = wLayoutParams

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(false)
        } else {
            dialog.setCancelable(false)
        }
        dialog.show()

        val textCo = dialog.findViewById<TextView>(R.id.text_co)
        val textKhong = dialog.findViewById<TextView>(R.id.text_khong)

        textCo.setOnClickListener {
            categoryViewModel.deleteCategory(category)
            dialog.dismiss()
            findNavController().popBackStack()
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }
}