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
import com.example.qltaichinhcanhan.main.library.CustomDialog
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.DataColor
import com.example.qltaichinhcanhan.main.model.Icon
import com.example.qltaichinhcanhan.main.model.ImageCheckCircle
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class EditCategoryFragment : BaseFragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var dataViewMode: DataViewMode
    var editOrAddCategory = Category()
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
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initEvent()
    }

    private fun initView() {
        // update lại list category ( lấy trong khoảng để trùng thì hiện thị)

        adapterIconCategory =
            AdapterIconCategory(requireContext(),
                DefaultData.getListCategoryAdd(),
                AdapterIconCategory.LayoutType.TYPE2)
        binding.rcvIconCategory.adapter = adapterIconCategory
        val myLinearLayoutManager =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager
        adapterIConColor = AdapterIConColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        editOrAddCategory = dataViewMode.editOrAddCategory
        binding.edtPlannedOutlay.addTextChangedListener(MoneyTextWatcher(binding.edtPlannedOutlay))

        checkEditOrAddCategory(editOrAddCategory)
        checkSelectIconCategory()

    }

    private fun checkSelectIconCategory() {
        val ck = dataViewMode.selectIconR.id
        if (ck > 2) {
            binding.imgIconCategory.setImageResource(IconR.getIconById(requireContext(),
                ck,
                IconR.listIconRCategory))
            dataViewMode.editOrAddCategory.icon = ck
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        adapterIconCategory.setClickItemSelect {
            if (it.idCategory == 1) {
                findNavController().navigate(R.id.action_editCategoryFragment_to_iconCatalogFragment)
            } else {
                binding.imgIconCategory.setImageResource(IconR.getIconById(requireContext(),
                    it.icon!!,
                    IconR.listIconRCategory))
                dataViewMode.editOrAddCategory.icon = it.icon
            }
        }


        adapterIConColor.setClickItemSelect {
            binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(),
                it.idColorR!!,
                IconR.getListIconCheckCircle()))
            adapterIconCategory.updateColor(it.idColorR!!)
            dataViewMode.editOrAddCategory.color = it.idColorR

        }

        binding.textSaveCategory.setOnClickListener {
            if (checkData(1)) {
                Log.e("data", "class category save: ${dataViewMode.editOrAddCategory.toString()}")
                dataViewMode.updateCategory(category = editOrAddCategory)
                findNavController().popBackStack()
            }
        }

        binding.textCreateCategory.setOnClickListener {
            if (checkData(2)) {
                Log.e("data", "class category save: ${dataViewMode.editOrAddCategory.toString()}")
                dataViewMode.addCategory(category = editOrAddCategory)
                findNavController().popBackStack()
            }
        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, category = editOrAddCategory)
        }
    }

    private fun checkEditOrAddCategory(category: Category) {
        binding.imgIconCategory.setImageResource(IconR.getIconById(requireContext(),
            category.icon!!,
            IconR.listIconRCategory))
        binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(),
            category.color!!,
            IconR.getListIconCheckCircle()))


        if (category.idCategory == 1 || category.idCategory == 2) {
            binding.textTitleTotal.setText(R.string.create_category)
            binding.llUpdateCategory.visibility = View.GONE
            binding.txtTypeCategory.visibility = View.GONE
            binding.textCreateCategory.visibility = View.VISIBLE
            binding.llTypeCategory.visibility = View.VISIBLE
            if (category.type == CategoryType.EXPENSE) {
                binding.imgExpense.isActivated = true
                binding.imgInCome.isActivated = false
            } else if (category.type == CategoryType.INCOME) {
                binding.imgExpense.isActivated = false
                binding.imgInCome.isActivated = true
            }
            binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(),
                1,
                IconR.getListIconCheckCircle()))

        } else {
            binding.edtNameCategory.setText(category.categoryName)
            binding.edtPlannedOutlay.setText(convertFloatToString(category.moneyLimit!!))
            binding.llUpdateCategory.visibility = View.VISIBLE
            binding.txtTypeCategory.visibility = View.VISIBLE
            binding.textCreateCategory.visibility = View.GONE
            binding.llTypeCategory.visibility = View.GONE
            binding.txtTypeCategory.text = getTypeCategory(category.type.toString())
            adapterIConColor.updateSelectColor(category.color!!)
            adapterIconCategory.updateColor(category.color!!)

        }


    }

    private fun checkData(typeCick: Int): Boolean {
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),
                requireContext().resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT).show()
            return false
        }
        dataViewMode.editOrAddCategory.categoryName = textName.toString()

        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtPlannedOutlay.text.toString())
        val temp = value.toString()
        if (binding.edtPlannedOutlay.text.isNotEmpty()) {
            try {
                val number = temp.toFloat()
                dataViewMode.editOrAddCategory.moneyLimit = number
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(),
                    requireContext().resources.getString(R.string.you_entered_the_wrong_format),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            dataViewMode.editOrAddCategory.moneyLimit = 0F
        }

        if (typeCick == 2) {
            dataViewMode.editOrAddCategory.idCategory = 0
            if (dataViewMode.editOrAddCategory.icon!! <= 2) {
                Toast.makeText(requireContext(),
                    requireContext().resources.getString(R.string.category_names_cannot_be_left_blank),
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun createDialogDelete(gravity: Int, category: Category) {
        val customDialog = CustomDialog(requireActivity())
        customDialog.showDialog(
            Gravity.CENTER,
            resources.getString(R.string.dialog_message),
            resources.getString(R.string.category_delete_confirmation),
            resources.getString(R.string.text_ok),
            {
                dataViewMode.deleteCategory(category)
                customDialog.dismiss()
                findNavController().popBackStack()
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )
    }

    private fun getTypeCategory(type: String): String {
        if (type == CategoryType.EXPENSE.toString()) {
            return requireContext().resources.getString(R.string.expense)
        } else if (type == CategoryType.INCOME.toString()) {
            return requireContext().resources.getString(R.string.in_come)
        }
        return ""
    }

    override fun onDestroy() {
        Log.e("test", "Edt: onDestroy")
        dataViewMode.resetDataCategory()
        super.onDestroy()
    }

}