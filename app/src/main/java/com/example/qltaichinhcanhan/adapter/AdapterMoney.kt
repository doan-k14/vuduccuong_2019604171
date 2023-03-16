package com.example.qltaichinhcanhan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.databinding.ItemMoneyESBinding
import com.example.qltaichinhcanhan.databinding.ItemMoneyExpenseBinding
import com.example.qltaichinhcanhan.mode.Category
import com.example.qltaichinhcanhan.mode.Money
import java.text.DecimalFormat
import java.text.NumberFormat

class AdapterMoney(
    var context: Context,
    var list: List<Money>,
    var arrayCategory: List<Category>,
    var layoutType: LayoutType, //Thêm biến kiểu enum
) : RecyclerView.Adapter<AdapterMoney.ViewHolder>() {

    enum class LayoutType {
        TYPE1,
        TYPE2
    }

    inner class ViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ViewBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewBinding

        when (layoutType) {
            LayoutType.TYPE1 -> {
                binding = ItemMoneyExpenseBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                binding = ItemMoneyESBinding.inflate(inflater, parent, false)
            }
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder) {
            // Dùng ViewHolder để ánh xạ view trong layout
            when (layoutType) {
                LayoutType.TYPE1 -> {
                    val itemBinding = binding as ItemMoneyExpenseBinding

                    if (item.type == 1) {
                        itemBinding.root.isActivated = true
                    } else if (item.type == 2) {
                        itemBinding.root.isActivated = false
                    }

                    for (i in arrayCategory) {
                        if (item.category == i.id) {
                            binding.txtCategory.text = i.name
                        }
                    }
                    var nameCurrency = ""
                    if (item.currency == 1) {
                        nameCurrency = "VND"
                    } else if (item.currency == 2) {
                        nameCurrency = "USD"
                    }
                    binding.txtDate.text = item.day.toString() + "/" + item.month + "/" + item.year

                    val formatter: NumberFormat = DecimalFormat("#,###")
                    binding.txtMoney.text = formatter.format(item.amount) + " " + nameCurrency
                    binding.txtNote.text = item.note.toString()

                    binding.root.setOnClickListener {
                        clickItemSelect?.let {
                            it(item)
                        }
                    }


                }
                LayoutType.TYPE2 -> {
                    val itemBinding = binding as ItemMoneyESBinding

                    if (item.type == 1) {
                        itemBinding.root.isActivated = true
                    } else if (item.type == 2) {
                        itemBinding.root.isActivated = false
                    }

                    for (i in arrayCategory) {
                        if (item.category == i.id) {
                            binding.txtCategory.text = i.name
                        }
                    }
                    var nameCurrency = ""
                    if (item.currency == 1) {
                        nameCurrency = "VND"
                    } else if (item.currency == 2) {
                        nameCurrency = "USD"
                    }

                    binding.txtDate.text = item.day.toString() + "/" + item.month + "/" + item.year
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    binding.txtMoney.text = formatter.format(item.amount) + " " + nameCurrency
                    binding.txtNote.text = item.note.toString()

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<Money>, newCategory: List<Category>) {
        list = newList
        arrayCategory = newCategory
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }

    private var clickItemSelect: ((Money) -> Unit)? = null
    fun setClickItemSelect(listener: (Money) -> Unit) {
        clickItemSelect = listener
    }


}
