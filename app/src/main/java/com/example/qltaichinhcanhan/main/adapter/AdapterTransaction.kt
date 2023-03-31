package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemTransactionBinding
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.query_model.FilterTransactions
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import java.text.DecimalFormat

class AdapterTransaction(
    var context: Context,
    var listCategory: List<FilterTransactions>,
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
            binding.imgCategory.setImageResource(IconR.getIconById(context,
                item.transactionWithDetails.category!!.icon!!,IconR.listIconRCategory))
            binding.imgCategory.setBackgroundResource(IconR.getColorById(context,
                item.transactionWithDetails.category!!.color!!,IconR.getListColorIconR()))
            binding.textNameCategory.text = item.transactionWithDetails.category.categoryName

            val formatter = DecimalFormat("#,###")
            val m = formatter.format(item.transactionWithDetails.transaction!!.transactionAmount)
            binding.textValueCategory.text = m

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

    fun updateData(newList: List<FilterTransactions>) {
        this.listCategory = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((FilterTransactions) -> Unit)? = null

    private var clickLongItemSelect: ((FilterTransactions) -> Unit)? = null

    fun setClickItemSelect(listener: (FilterTransactions) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (FilterTransactions) -> Unit) {
        clickLongItemSelect = listener
    }
}