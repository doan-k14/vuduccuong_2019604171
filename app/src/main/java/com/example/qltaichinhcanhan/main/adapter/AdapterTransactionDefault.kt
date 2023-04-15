package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemTransactionBinding
import com.example.qltaichinhcanhan.main.inf.TransactionClickListener
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import java.text.DecimalFormat

class AdapterTransactionDefault(
    var context: Context,
    var listCategory: List<TransactionWithFullDetails>,
    var transactionClickListener: TransactionClickListener
) : RecyclerView.Adapter<AdapterTransactionDefault.ViewHolder>() {

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
                item.transactionWithDetails?.category!!.icon!!,IconR.listIconRCategory))
            binding.imgCategory.setBackgroundResource(IconR.getColorById(context,
                item.transactionWithDetails?.category!!.color!!,IconR.getListColorIconR()))
            binding.textNameCategory.text =  item.transactionWithDetails?.category!!.categoryName

            val formatter = DecimalFormat("#,###")
            val m = formatter.format( item.transactionWithDetails?.transaction!!.transactionAmount!!)
            binding.textValueCategory.text =  "${m} ${item.moneyAccountWithDetails?.country?.currencySymbol}"

            holder.itemView.setOnClickListener {
                transactionClickListener.onTransactionClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }
}