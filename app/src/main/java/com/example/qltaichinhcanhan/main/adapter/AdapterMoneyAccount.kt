package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import java.text.DecimalFormat

class AdapterMoneyAccount(
    var context: Context,
    var listCategory: List<MoneyAccountWithDetails>,
    var layoutType: LayoutType,
) : RecyclerView.Adapter<AdapterMoneyAccount.ViewHolder>() {

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
                binding = ItemTransactionBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                binding = ItemTransactionDialogBinding.inflate(inflater, parent, false)
            }
        }

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        val formatter = DecimalFormat("#,###")
        with(holder) {
            when (layoutType) {
                LayoutType.TYPE1 -> {
                    binding as ItemTransactionBinding
                    binding.imgCategory.setImageResource(IconR.getIconById(context,
                        item.moneyAccount!!.icon!!,
                        IconR.listIconRAccount))

                    binding.imgCategory.setBackgroundResource(IconR.getColorById(context,
                        item.moneyAccount.color!!,
                        IconR.getListColorIconR()))
                    binding.textNameCategory.text = item.moneyAccount!!.moneyAccountName
                    val m = formatter.format(item.moneyAccount!!.amountMoneyAccount)
                    binding.textValueCategory.text = "${m} ${item.country!!.currencySymbol}"

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
                LayoutType.TYPE2 -> {
                    binding as ItemTransactionDialogBinding

//                    Log.e("data","${item.moneyAccount!!.select}")
                    binding.imgSelect.isActivated = item.moneyAccount!!.selectMoneyAccount == true

                    binding.imgCategory.setImageResource(IconR.getIconById(context,
                        item.moneyAccount.icon!!,
                        IconR.listIconRAccount))

                    binding.imgCategory.setBackgroundResource(IconR.getColorById(context,
                        item.moneyAccount.color!!,
                        IconR.getListColorIconR()))
                    binding.textNameCategory.text = item.moneyAccount!!.moneyAccountName

                    val m = formatter.format(item.moneyAccount!!.amountMoneyAccount)
                    binding.textValueCategory.text = "${m} ${item.country!!.currencySymbol}"

                    binding.root.setOnClickListener {
                        for (i in listCategory.indices) {
                            listCategory[i].moneyAccount!!.selectMoneyAccount = (i == position)
                        }
                        clickItemSelect?.let {
                            it(item)
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun updateData(newList: List<MoneyAccountWithDetails>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateSelectTransaction(idTransaction: Int) {
        for (i in listCategory) {
            i.moneyAccount!!.selectMoneyAccount = (i.moneyAccount.idMoneyAccount == idTransaction)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((MoneyAccountWithDetails) -> Unit)? = null

    private var clickLongItemSelect: ((MoneyAccountWithDetails) -> Unit)? = null

    fun setClickItemSelect(listener: (MoneyAccountWithDetails) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (MoneyAccountWithDetails) -> Unit) {
        clickLongItemSelect = listener
    }
}