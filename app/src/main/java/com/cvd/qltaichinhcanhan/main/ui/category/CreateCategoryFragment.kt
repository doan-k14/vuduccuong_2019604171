package com.cvd.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCreateCategoryBinding
import com.cvd.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_r.CategoryType
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterIConColor
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsColor
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore

class CreateCategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCreateCategoryBinding
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var dataViewMode: DataViewMode
    var mCreateCategory = Category()
    var listCategory = listOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()

    }

    private fun initView() {
        adapterIConColor = AdapterIConColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        binding.imgIconCategory.setImageResource(
            UtilsColor.setImageByName(
                requireContext(),
                dataViewMode.createCategory.idIcon.toString()
            )
        )
        binding.imgIconCategory.setBackgroundResource(
            UtilsColor.setBackgroundCircleCategoryById(
                requireContext(),
                dataViewMode.createCategory.idColor!!
            )
        )

        adapterIConColor.updateSelectColor(dataViewMode.createCategory.idColor!!)

        binding.edtPlannedOutlay.addTextChangedListener(MoneyTextWatcher(binding.edtPlannedOutlay))

        mCreateCategory = dataViewMode.createCategory

        binding.textTitleTotal.setText(R.string.create_category)
        binding.textCreateCategory.visibility = View.VISIBLE
        binding.llTypeCategory.visibility = View.VISIBLE
        if (mCreateCategory.type == 1) {
            binding.imgExpense.isActivated = true
            binding.imgExpense.visibility = View.VISIBLE
            binding.textExpense.visibility = View.VISIBLE
            binding.imgInCome.visibility = View.GONE
            binding.textInCome.visibility = View.GONE
            binding.imgInCome.isActivated = false
        } else if (mCreateCategory.type == 2) {
            binding.imgExpense.isActivated = false
            binding.imgInCome.isActivated = true
            binding.imgExpense.visibility = View.GONE
            binding.textExpense.visibility = View.GONE
            binding.imgInCome.visibility = View.VISIBLE
            binding.textInCome.visibility = View.VISIBLE
        }

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgIconMore.setOnClickListener {
            findNavController().navigate(R.id.action_createCategoryFragment_to_iconCatalogFragment)
        }

        adapterIConColor.setClickItemSelect {
            binding.imgIconCategory.setBackgroundResource(
                IconR.getIconById(
                    requireContext(),
                    it.idColorR!!,
                    IconR.getListIconCheckCircle()
                )
            )
            dataViewMode.createCategory.idColor = it.idColorR
        }


        binding.textCreateCategory.setOnClickListener {
            if (checkData(dataViewMode.listCategoryByType)) {
                createCategory()
            } else {
                Utils.showToast(requireContext(), "Vui lòng nhập dữ liệu đúng định dạng")
            }
        }
    }

    private fun createCategory() {
        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoading()
        val userAccount = Utils.getUserAccountLogin(requireContext())
        mCreateCategory.idUserAccount = userAccount.idUserAccount

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBCreateCategory(object : UtilsFireStore.CBCreateCategory {
            override fun createSuccess() {
                loadingDialog.hideLoading()
                mCreateCategory = Category()
                dataViewMode.createCategory = Category()
                findNavController().popBackStack()
            }

            override fun createFailed() {
                loadingDialog.hideLoading()
                Utils.showToast(requireContext(), "Create category failed")
            }

        })

        utilsFireStore.createCategory(mCreateCategory)

    }

    private fun checkData(list: List<Category>): Boolean {
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT
            ).show()
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
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.you_entered_the_wrong_format),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            plannedOutlay = 0F
        }


        for (category in list) {
            if (category.categoryName == textName.toString()) {
                return false
            }
        }
        mCreateCategory = Category(
            "",
            textName.toString(),
            mCreateCategory.type,
            plannedOutlay,
            false,
            mCreateCategory.idIcon,
            mCreateCategory.idColor,
        )
        return true
    }
}