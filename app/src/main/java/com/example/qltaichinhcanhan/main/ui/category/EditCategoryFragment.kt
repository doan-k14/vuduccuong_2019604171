package com.example.qltaichinhcanhan.main.ui.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.splash.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.Category
import com.example.qltaichinhcanhan.main.model.DataColor
import com.example.qltaichinhcanhan.main.model.Icon
import com.example.qltaichinhcanhan.main.model.IconCategoryData
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode


class EditCategoryFragment : BaseFragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var categoryViewModel: CategoryViewMode
    var category = Category()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.e("test", "edt: onCreateView")
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]
        initView()
        initEvent()
    }

    private fun initView() {
        category = categoryViewModel.category
        categoryViewModel.icon.name = category.icon!!
        categoryViewModel.icon.color = category.color!!
        if (categoryViewModel.nameIcon != null) {
            categoryViewModel.icon.name = categoryViewModel.nameIcon
            category.icon = categoryViewModel.nameIcon
        }

        checkTypeCategory(category)

        val list = DataColor.getListCategory()
        val listColor = DataColor.getListCheckCircle()

        adapterIconCategory =
            AdapterIconCategory(requireContext(), list, AdapterIconCategory.LayoutType.TYPE2)
        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        adapterIConColor = AdapterIConColor(requireContext(), listColor)
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

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


        adapterIConColor.setClickItemSelect {
            val id = DataColor.getIdColorById(it.idColor)
            binding.imgIconCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(
                requireContext(),
                id!!))
            adapterIconCategory.updateColor(it.idColor)
            categoryViewModel.icon.color = it.idColor
        }

        binding.textSaveCategory.setOnClickListener {
            checkData(1)
            categoryViewModel.updateCategory(category)
            findNavController().popBackStack()
        }

        binding.textCreateCategory.setOnClickListener {
            checkData(2)
            categoryViewModel.addCategory(category)
            findNavController().popBackStack()
        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, category)
        }
    }

    private fun checkTypeCategory(category: Category) {
        binding.imgIconCategory.setImageResource(IconCategoryData.showICon(requireContext(),
            category.icon!!))
        if (category.id == 1 || category.id == 2) {
            binding.textTitleTotal.setText(R.string.create_category)
            binding.llUpdateCategory.visibility = View.GONE
            binding.txtTypeCategory.visibility = View.GONE
            binding.textCreateCategory.visibility = View.VISIBLE
            binding.llTypeCategory.visibility = View.VISIBLE
            if (category.type!! == 1) {
                binding.imgExpense.isActivated = true
                binding.imgInCome.isActivated = false
            } else {
                binding.imgExpense.isActivated = false
                binding.imgInCome.isActivated = true
            }
        } else {
            binding.edtNameCategory.setText(category.nameCategory)
            binding.edtPlannedOutlay.setText(category.lave.toString())
            categoryViewModel.icon.name = category.icon!!
            binding.llUpdateCategory.visibility = View.VISIBLE
            binding.txtTypeCategory.visibility = View.VISIBLE
            binding.textCreateCategory.visibility = View.GONE
            binding.llTypeCategory.visibility = View.GONE
            binding.txtTypeCategory.text = getTypeCategory(category.type!!)
        }
    }

    private fun clearData() {
        categoryViewModel.category = Category()
        categoryViewModel.icon = Icon()
        categoryViewModel.nameIcon = null
    }

    private fun checkData(typeCick: Int): Boolean {
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),
                "Tên danh mục không được bỏ trống!",
                Toast.LENGTH_SHORT).show()
            return false
        }
        category.nameCategory = textName.toString()

        var textPlannedOutlay = binding.edtPlannedOutlay.text
        if (typeCick == 2) {
            category.id = 0
            if (categoryViewModel.icon.name == "ic_add") {
                Toast.makeText(requireContext(),
                    "Tên danh mục không được bỏ trống!",
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }

        category.icon = categoryViewModel.icon.name
        category.color = categoryViewModel.icon.color
        return true
    }

    private fun createDialogDelete(gravity: Int, category: Category) {
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

    private fun getTypeCategory(type: Int): String {
        if (type == 1) {
            return "Chi phí"
        } else if (type == 2) {
            return "Thu nhập"
        }
        return ""
    }

    override fun onCallBackICon(icon: Icon) {
        Log.e("test", "Edt: onCallBackICon ${icon.name}")
        categoryViewModel.icon.name = icon.name
        super.onCallBackICon(icon)
    }

    override fun onDestroyView() {
        Log.e("test", "Edt: onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.e("test", "Edt: onDestroy")
        clearData()
        super.onDestroy()
    }

}