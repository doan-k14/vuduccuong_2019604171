package com.cvd.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterColor
import com.cvd.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP
import com.cvd.qltaichinhcanhan.utils.UtilsColor
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class EditCategoryFragment : BaseFragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterColor: AdapterColor

    private lateinit var dataViewMode: DataViewMode
    var mEditCategory = Category()
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

        mEditCategory = dataViewMode.editCategory

        initView()
        initEvent()
    }


    private fun initView() {
        adapterColor = AdapterColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapterColor.updateSelectColor(mEditCategory.idColor!!)

        binding.edtPlannedOutlay.addTextChangedListener(MoneyTextWatcher(binding.edtPlannedOutlay))

        binding.edtNameCategory.setText(mEditCategory.categoryName)
        binding.edtPlannedOutlay.setText(convertFloatToString(mEditCategory.moneyLimit!!))
        binding.llUpdateCategory.visibility = View.VISIBLE
        binding.txtTypeCategory.visibility = View.VISIBLE
        binding.llTypeCategory.visibility = View.GONE
        binding.txtTypeCategory.text = getTypeCategory(mEditCategory.type!!)

        binding.imgIconCategory.setImageResource(
            UtilsColor.setImageByName(
                requireContext(),
                mEditCategory.idIcon.toString()
            )
        )
        binding.imgIconCategory.setBackgroundResource(
            UtilsColor.setBackgroundCircleCategoryById(
                requireContext(),
                mEditCategory.idColor!!
            )
        )

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgIconMore.setOnClickListener {
            findNavController().navigate(R.id.action_editCategoryFragment_to_iconCatalogFragment)
        }

        adapterColor.setClickItemSelect {
            binding.imgIconCategory.setBackgroundResource(
                IconR.getIconById(
                    requireContext(),
                    it.idColorR!!,
                    IconR.getListIconCheckCircle()
                )
            )
            dataViewMode.editCategory.idColor = it.idColorR
        }

        binding.textSaveCategory.setOnClickListener {
            if (checkData(dataViewMode.listCategoryByType)) {
                updateCategory()
                UtilsSharedP.showToast(requireContext(), "Cập nhật thành công")
            } else {
//                findNavController().popBackStack()
            }
        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, category = mEditCategory)
        }
    }

    private fun updateCategory() {
        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoading()

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBUpdateCategory(object : UtilsFireStore.CBUpdateCategory {
            override fun updateSuccess() {
                loadingDialog.hideLoading()
                mEditCategory = Category()
                dataViewMode.editCategory = Category()
                findNavController().popBackStack()
            }

            override fun updateFailed() {
                loadingDialog.hideLoading()
                UtilsSharedP.showToast(requireContext(), "Create category failed")
            }
        })

        utilsFireStore.updateCategoryById(mEditCategory.idCategory, mEditCategory)
    }

    private fun checkData(list: List<Category>): Boolean {
        val textName = binding.edtNameCategory.text
        if (textName.isEmpty()) {
            UtilsSharedP.showToast(
                requireContext(),
                requireContext().resources.getString(R.string.category_names_cannot_be_left_blank),
            )

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
                UtilsSharedP.showToast(
                    requireContext(),
                    requireContext().resources.getString(R.string.you_entered_the_wrong_format)
                )
                return false
            }
        } else {
            plannedOutlay = 0F
        }

//        if (mEditCategory.categoryName == textName.toString() && mEditCategory.moneyLimit == plannedOutlay
//            && mEditCategory.idIcon == dataViewMode.editDefaultCategory.idIcon
//            && mEditCategory.idColor == dataViewMode.editDefaultCategory.idColor
//        ) {
//            Utils.showToast(
//                requireContext(),
//                "Cập nhật thành công"
//            )
//            findNavController().popBackStack()
//            return false
//        }
//
//
//        for (category in list) {
//            if (category.categoryName == textName.toString()) {
//                Utils.showToast(
//                    requireContext(),
//                    "Tên danh mục đã tồn tại trong danh sách, hay thay mới"
//                )
//                return false
//            }
//        }

        mEditCategory = Category(
            mEditCategory.idCategory,
            textName.toString(),
            mEditCategory.type,
            plannedOutlay,
            false,
            mEditCategory.idIcon,
            mEditCategory.idColor,
            mEditCategory.idUserAccount,
        )
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
                val loadingDialog = LoadingDialog(requireContext())
                loadingDialog.showLoading()
                customDialog.dismiss()
                val utilsFireStore = UtilsFireStore()
                utilsFireStore.setCBDeleteCategory(object : UtilsFireStore.CBDeleteCategory {
                    override fun deleteSuccess() {
                        loadingDialog.hideLoading()
                        findNavController().popBackStack()
                    }

                    override fun deleteFailed() {
                        loadingDialog.hideLoading()
                        UtilsSharedP.showToast(requireContext(), "Delete category failed")
                    }
                })
                utilsFireStore.deleteCategoryById(category.idCategory)
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )
    }

    private fun getTypeCategory(type: Int): String {
        if (type == 1) {
            return requireContext().resources.getString(R.string.expense)
        } else if (type == 2) {
            return requireContext().resources.getString(R.string.in_come)
        }
        return ""
    }
}