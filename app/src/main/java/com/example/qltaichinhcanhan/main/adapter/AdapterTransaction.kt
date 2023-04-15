package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemTransactionBinding
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import java.text.DecimalFormat

class AdapterTransaction(
    var context: Context,
    var listCategory: List<FilterTransactions>,
    var currencyCode: String,
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
        var totalAmount = 0F
        for (i in listCategory) {
            totalAmount += i.transaction.transactionWithDetails?.transaction?.transactionAmount!!
        }
        val item = listCategory[position]
        with(holder) {
            binding.imgCategory.setImageResource(IconR.getIconById(context,
                item.transaction.transactionWithDetails?.category!!.icon!!,
                IconR.listIconRCategory))
            binding.imgCategory.setBackgroundResource(IconR.getColorById(context,
                item.transaction.transactionWithDetails?.category!!.color!!,
                IconR.getListColorIconR()))
            binding.textNameCategory.text =
                item.transaction.transactionWithDetails?.category!!.categoryName

            val transactionAmount =
                item.transaction.transactionWithDetails!!.transaction!!.transactionAmount!!
            val percentage = (transactionAmount / totalAmount) * 100
            val formatter = DecimalFormat("#,###")
            binding.textValueCategory.text =
                "(${percentage.toInt()}%)   ${formatter.format(transactionAmount)} ${currencyCode}"

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

    fun updateCurrencyCode(currencyCodeNew: String) {
        this.currencyCode = currencyCodeNew
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