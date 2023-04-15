package com.example.qltaichinhcanhan.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.inf.TransactionClickListener
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionsShowDetails

class AdapterTransactionByTime(
    var context: Context,
    private val listTransaction: List<TransactionsShowDetails>,
    var transactionClickListener: TransactionClickListener,
    var type: Int,
) : RecyclerView.Adapter<AdapterTransactionByTime.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val moneyLimit: TextView = itemView.findViewById(R.id.text_moneyLimit)
        val titleMoneyLimit: TextView = itemView.findViewById(R.id.text_title_moneyLimit)
        val iconList: RecyclerView = itemView.findViewById(R.id.icon_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_by_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactionsShowDetails = listTransaction[position]
        holder.categoryName.text = transactionsShowDetails.day


        var totalAmount = 0F
        for (i in transactionsShowDetails.transactions) {
            totalAmount += i.transactionWithDetails?.transaction?.transactionAmount!!
        }
        val moneyLimit =
            transactionsShowDetails.transactions[0].transactionWithDetails?.category?.moneyLimit!!
        if (type != 1) {
            if (moneyLimit != 0F) {
                if (moneyLimit < totalAmount) {
                    holder.moneyLimit.visibility = View.VISIBLE
                    holder.titleMoneyLimit.visibility = View.VISIBLE
                    val limit = (totalAmount - moneyLimit) / moneyLimit * 100
                    holder.titleMoneyLimit.text = context.resources.getString(R.string.exceeding_the_limit)
                    holder.moneyLimit.text = " ${limit.toInt()}%"
                } else {
                    holder.moneyLimit.visibility = View.GONE
                    holder.titleMoneyLimit.visibility = View.GONE
                }
            }
        }


        val layoutManager =
            object : GridLayoutManager(context, 1, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        holder.iconList.layoutManager = layoutManager
        holder.iconList.adapter = AdapterTransactionDefault(context,
            transactionsShowDetails.transactions,
            transactionClickListener)
    }

    override fun getItemCount() = listTransaction.size
}
