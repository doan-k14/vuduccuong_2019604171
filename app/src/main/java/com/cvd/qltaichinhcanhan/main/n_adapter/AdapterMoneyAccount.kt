package com.cvd.qltaichinhcanhan.main.n_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.cvd.qltaichinhcanhan.databinding.*
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.utils.UtilsColor
import java.text.DecimalFormat

class AdapterMoneyAccount(
    var context: Context,
    var listCategory: List<MoneyAccount>,
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

        val binding: ViewBinding = when (layoutType) {
            LayoutType.TYPE1 -> {
                ItemTransactionBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                ItemTransactionDialogBinding.inflate(inflater, parent, false)
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
                    binding.imgCategory.setImageResource(UtilsColor.setImageByName(context, iconName = item.icon.idIConVD.toString()))
                    binding.imgCategory.setBackgroundResource(UtilsColor.setBackgroundCircleCategoryById(context,item.icon.idColor!!))
                    binding.textNameCategory.text = item.moneyAccountName
                    val m = formatter.format(item.amountMoneyAccount)
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

                    binding.imgSelect.isActivated = item.selectMoneyAccount == true
                    binding.imgCategory.setImageResource(UtilsColor.setImageByName(context, iconName = item.icon.idIConVD.toString()))
                    binding.imgCategory.setBackgroundResource(UtilsColor.setBackgroundCircleCategoryById(context,item.icon.idColor!!))
                    binding.textNameCategory.text = item.moneyAccountName
                    val m = formatter.format(item.amountMoneyAccount)
                    binding.textValueCategory.text = "${m} ${item.country!!.currencySymbol}"


                    binding.root.setOnClickListener {
                        for (i in listCategory.indices) {
                            listCategory[i].selectMoneyAccount = (i == position)
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

    fun updateData(newList: List<MoneyAccount>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateSelectTransaction(idTransaction: String) {
        for (i in listCategory) {
            i.selectMoneyAccount = (i.idMoneyAccount == idTransaction)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((MoneyAccount) -> Unit)? = null

    private var clickLongItemSelect: ((MoneyAccount) -> Unit)? = null

    fun setClickItemSelect(listener: (MoneyAccount) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (MoneyAccount) -> Unit) {
        clickLongItemSelect = listener
    }
}