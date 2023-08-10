package com.cvd.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterIConColor
import com.cvd.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_r.CategoryType
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterIconCategory
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode


class EditCategoryFragment : BaseFragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var dataViewMode: DataViewMode
    var editOrAddCategory = Category()
    var listCategory = listOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("TAG", "onDestroyView: ", )
    }
    private fun initView() {
        adapterIConColor = AdapterIConColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        if(dataViewMode.selectIconR != ""){
            binding.imgIconCategory.setImageResource(IconR.showIconByName(requireContext(), dataViewMode.selectIconR))
            adapterIConColor.updateSelectColor(dataViewMode.idColor)
        }

        binding.edtPlannedOutlay.addTextChangedListener(MoneyTextWatcher(binding.edtPlannedOutlay))

        editOrAddCategory = dataViewMode.editOrAddCategory
        checkEditOrAddCategory(editOrAddCategory)

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgIconMore.setOnClickListener {
            findNavController().navigate(R.id.action_editCategoryFragment_to_iconCatalogFragment)
        }

        adapterIConColor.setClickItemSelect {
            binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(), it.idColorR!!, IconR.getListIconCheckCircle()))
            dataViewMode.editOrAddCategory.idColor = it.idColorR
            dataViewMode.idColor = it.idColorR!!
        }

        val listCategory = dataViewMode.listCategoryByType
        binding.textSaveCategory.setOnClickListener {
            Log.e("TAG", "initEvent: "+checkData(listCategory) )
            if (checkData(listCategory)) { // check xem đã nhập hết các thông số chưa
                // với category name check xem đã tồn tại trên fire base chưa
//                dataViewMode.updateCategory(category = editOrAddCategory)
                findNavController().popBackStack()
            }
        }

        binding.textCreateCategory.setOnClickListener {
//            if (checkData()) {
//                Log.e("data", "class category save: ${dataViewMode.editOrAddCategory.toString()}")
////                dataViewMode.addCategory(category = editOrAddCategory)
//                findNavController().popBackStack()
//            }
            Log.e("TAG", "initEvent: "+checkData(listCategory) )

        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, category = editOrAddCategory)
        }
    }

    private fun checkEditOrAddCategory(category: Category) {

        if (category.categoryName == "Thêm") {
            binding.textTitleTotal.setText(R.string.create_category)
            binding.llUpdateCategory.visibility = View.GONE
            binding.txtTypeCategory.visibility = View.GONE
            binding.textCreateCategory.visibility = View.VISIBLE
            binding.llTypeCategory.visibility = View.VISIBLE
            if (category.type == 1) {
                binding.imgExpense.isActivated = true
                binding.imgExpense.visibility = View.VISIBLE
                binding.textExpense.visibility = View.VISIBLE
                binding.imgInCome.visibility = View.GONE
                binding.textInCome.visibility = View.GONE
                binding.imgInCome.isActivated = false
            } else if (category.type == 2) {
                binding.imgExpense.isActivated = false
                binding.imgInCome.isActivated = true
                binding.imgExpense.visibility = View.GONE
                binding.textExpense.visibility = View.GONE
                binding.imgInCome.visibility = View.VISIBLE
                binding.textInCome.visibility = View.VISIBLE
            }
            binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(), category.idColor!!, IconR.getListIconCheckCircle()))
        } else {
            binding.edtNameCategory.setText(category.categoryName)
            binding.edtPlannedOutlay.setText(convertFloatToString(category.moneyLimit!!))
            binding.llUpdateCategory.visibility = View.VISIBLE
            binding.txtTypeCategory.visibility = View.VISIBLE
            binding.textCreateCategory.visibility = View.GONE
            binding.llTypeCategory.visibility = View.GONE
            binding.txtTypeCategory.text = getTypeCategory(category.type.toString())
            adapterIConColor.updateSelectColor(category.idColor!!)
            adapterIconCategory.updateColor(category.idColor!!)
            binding.imgIconCategory.setImageResource(IconR.showIconByName(requireContext(), category.idIcon!!))
            binding.imgIconCategory.setBackgroundResource(IconR.getIconById(requireContext(), 1, IconR.getListIconCheckCircle()))
        }


    }

    private fun checkData(list: List<Category>): Boolean {
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),
                requireContext().resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT).show()
            return false
        }

        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtPlannedOutlay.text.toString())
        val temp = value.toString()
        var plannedOutlay = 0F
        if (binding.edtPlannedOutlay.text.isNotEmpty()) {
            try {
                val number = temp.toFloat()
                plannedOutlay = number
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(),
                    requireContext().resources.getString(R.string.you_entered_the_wrong_format),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            plannedOutlay = 0F
        }

        var idIcon = ""
        var idColor = 0
        if(dataViewMode.selectIconR != ""){
            idIcon = dataViewMode.selectIconR
            idColor = dataViewMode.idColor
        }else{
            return false
        }
        for(category in list){
            if(category.categoryName == textName.toString()){
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