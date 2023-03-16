package com.example.qltaichinhcanhan.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.viewModel.MoneyViewModel
import com.example.qltaichinhcanhan.MoneyTextWatcher
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterCategory
import com.example.qltaichinhcanhan.databinding.FragmentExpenseBinding
import com.example.qltaichinhcanhan.mode.Category
import com.example.qltaichinhcanhan.mode.Money
import com.example.qltaichinhcanhan.viewModel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpenseFragment : Fragment() {
    private lateinit var binding: FragmentExpenseBinding
    private lateinit var adapterCategory: AdapterCategory
    private lateinit var moneyViewModel: MoneyViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private val calendar: Calendar = Calendar.getInstance()

    var category = 0
    lateinit var listCategory: ArrayList<Category>

    fun newInstance(): ExpenseFragment {
        val args = Bundle()
        val fragment = ExpenseFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moneyViewModel = ViewModelProvider(requireActivity())[MoneyViewModel::class.java]
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
        init()
    }

    private fun init() {
        initView()
    }

    private fun initView() {
        adapterCategory = AdapterCategory(requireActivity(), arrayListOf())
        binding.rcvCategory.adapter = adapterCategory
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        listCategory = arrayListOf()
        listCategory = creakArrayListCategory()
        creakData(listCategory)

        activity?.let {
            categoryViewModel.readAllData.observe(it) {
                listCategory = arrayListOf()
                for (i in it) {
                    if (i.type == 1) {
                        listCategory.add(i)
                    }
                }
                clearRadioCategory()
            }
        }

        adapterCategory.setClickItemSelect {
            for (i in listCategory) {
                i.select = i.id == it.id
            }
            category = it.id
            adapterCategory.updateData(listCategory)
        }

        adapterCategory.setClickLongItemSelect {
            createDialogUpdateOrDeleteCategory(Gravity.CENTER, it)
        }

        binding.btnAddCategory.setOnClickListener {
            createDialogAddCategory(Gravity.CENTER)
        }

        binding.edtExpenseAmount.addTextChangedListener(MoneyTextWatcher(binding.edtExpenseAmount))

        binding.edtDate.setOnClickListener {
            showDatePicker(binding.edtDate)
        }
        binding.btnAdd.setOnClickListener {
            if (binding.edtExpenseAmount.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Bạn chưa nhập số tiền",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (binding.edtDate.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Ngày tháng của khoản chi là rất quan trọng. Hãy nhập!",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val money = getAllMoney()
            if (money.amount!! <= 0) {
                Toast.makeText(requireContext(), "Bạn chưa nhập số tiền!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                moneyViewModel.addMoney(money)
                Toast.makeText(requireContext(),
                    "Thêm thành công số tiền: ${money.amount}",
                    Toast.LENGTH_SHORT).show()
                clearText()
                clearRadioCategory()
                hideKeyboard(requireActivity())
            }
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun creakArrayListCategory(): ArrayList<Category> {
        var listCategory = arrayListOf<Category>()
        listCategory.add(Category(0, "Tiền nhà", 1, true))
        listCategory.add(Category(0, "Giao thông", 1, false))
        listCategory.add(Category(0, "Du lịch", 1, false))
        listCategory.add(Category(0, "Mua sắm", 1, false))
        listCategory.add(Category(0, "Rượu và đồ uống", 1, false))
        listCategory.add(Category(0, "Học tập", 1, false))
        return listCategory
    }

    private fun creakData(listCategory: ArrayList<Category>) {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("categoryE", Context.MODE_PRIVATE)
        val checkCategory = sharedPreferences.getBoolean("saveCategoryE", false)
        if (!checkCategory) {
            for (i in listCategory) {
                categoryViewModel.addCategory(i)
            }
            val editor = sharedPreferences.edit()
            editor.putBoolean("saveCategoryE", true)
            editor.commit()
            Log.e("ccccc", "Khoi tao category thanh cong")
        }
    }

    private fun getAllMoney(): Money {
        var amount = 0
        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtExpenseAmount.text.toString())
        val temp = value.toString()
        if (temp != "") {
            try {
                amount = temp.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(super@ExpenseFragment.getContext(),
                    "Hãy nhập lại số tiền",
                    Toast.LENGTH_SHORT).show()
            }
        }
        val note = binding.edtNote.text.toString()

        var currency = 1

        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rd_vnd -> {
                currency = 1
            }
            R.id.rd_usd -> {
                currency = 2
            }
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date1 = dateFormat.parse(binding.edtDate.text.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val month1 = calendar.get(Calendar.MONTH) + 1 // Tháng được đánh số từ 0 đến 11
        val year1 = calendar.get(Calendar.YEAR)

        return Money(0, 1, day1, month1, year1, currency, amount, note, category)
    }

    private fun clearText() {
        binding.edtExpenseAmount.setText("")
        binding.edtNote.setText("")
        binding.edtDate.setText("")
        binding.edtExpenseAmount.requestFocus()
    }

    private fun clearRadioCategory() {
        for (i in listCategory) {
            i.select = false
        }
        if (listCategory.size > 0) {
            listCategory[0].select = true
        }
        adapterCategory.updateData(listCategory)
    }

    private fun createDialogAddCategory(gravity: Int) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_category)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
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
        val edtNameCategory = dialog.findViewById<TextView>(R.id.edt_name_category)
        val btnApp = dialog.findViewById<TextView>(R.id.btn_app_category)
        val imgClose = dialog.findViewById<ImageView>(R.id.img_close)

        btnApp.setOnClickListener {
            val txtNameCategory = edtNameCategory.text.trim().toString()
            var ckNameCategory = true
            for (i in listCategory) {
                if (i.name == txtNameCategory) {
                    ckNameCategory = false
                }
            }
            if (ckNameCategory) {
                categoryViewModel.addCategory(Category(0, txtNameCategory, 1, false))
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(),
                    "Đã tồn tại loại tiền, hãy nhập tên khác!",
                    Toast.LENGTH_SHORT).show()
            }
        }

        imgClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun createDialogUpdateOrDeleteCategory(gravity: Int, category: Category) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_update_or_delete_category)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
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

        val edtNameCategory = dialog.findViewById<TextView>(R.id.edt_name_category)

        edtNameCategory.text = category.name

        val btnUpdate = dialog.findViewById<TextView>(R.id.btn_app_category)
        val imgClose = dialog.findViewById<ImageView>(R.id.img_close)
        val imgDelete = dialog.findViewById<ImageView>(R.id.img_delete)

        btnUpdate.setOnClickListener {
            val txtNameCategory = edtNameCategory.text.toString()
            var newCategory = Category(category.id, txtNameCategory, 1, category.select)
            categoryViewModel.updateBook(newCategory)
            dialog.dismiss()
        }

        imgClose.setOnClickListener {
            dialog.dismiss()
        }

        imgDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Có") { _, _ ->
                categoryViewModel.deleteBook(category)
                dialog.dismiss()
            }
            builder.setNegativeButton("Không") { _, _ -> }
            builder.setTitle("Xóa ${category.name} ?")
            builder.setMessage("Xác nhận xóa thư mục: ${category.name} ?")
            builder.create().show()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString: String = dateFormat.format(calendar.time)
                editText.setText(dateString)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )

        // Thiết lập ngày tối đa là ngày hiện tại
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }


}