package com.example.qltaichinhcanhan.main.adapter_main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemTransactionBinding
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.m.Transaction
import com.example.qltaichinhcanhan.main.m.TransactionWithAccountAndCategoryName
import com.example.qltaichinhcanhan.mode.Category

class AdapterTransaction(
    var context: Context,
    var listCategory: ArrayList<TransactionWithAccountAndCategoryName>,
) : RecyclerView.Adapter<AdapterTransaction.ViewHolder>() {

    inner class ViewHolder(binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemTransactionBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        with(holder) {
            binding.imgCategory.setImageResource(DataColor.showBackgroundColorCircle(context, item.category1.icon!!))
            val color = DataColor.getIdColorById(item.category1.color!!)
            binding.imgCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(context,
                color.toString()))
            binding.textNameCategory.text = item.category1.nameCategory
            binding.textValueCategory.text = item.transaction.amountTransaction.toString()

            binding.root.setOnClickListener {
                clickItemSelect?.let {
                    it(item)
                }
            }

            binding.root.setOnLongClickListener {
                clickLongItemSelect?.let {
                    it(item)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun updateData(newList: ArrayList<TransactionWithAccountAndCategoryName>) {
        this.listCategory = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((TransactionWithAccountAndCategoryName) -> Unit)? = null

    private var clickLongItemSelect: ((TransactionWithAccountAndCategoryName) -> Unit)? = null

    fun setClickItemSelect(listener: (TransactionWithAccountAndCategoryName) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (TransactionWithAccountAndCategoryName) -> Unit) {
        clickLongItemSelect = listener
    }
}