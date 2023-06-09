package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.*

class AdapterAccount(
    var context: Context,
    var listCategory: ArrayList<Account>,
    var layoutType: LayoutType,
) : RecyclerView.Adapter<AdapterAccount.ViewHolder>() {

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
        with(holder) {
            when (layoutType) {
                LayoutType.TYPE1 -> {
                    binding as ItemTransactionBinding
                    binding.imgCategory.setImageResource(DataColor.showBackgroundColorCircle(context,
                        item.icon!!))
                    val color = DataColor.getIdColorById(item.color!!)
                    binding.imgCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(
                        context,
                        color.toString()))
                    binding.textNameCategory.text = item.nameAccount
                    binding.textValueCategory.text = item.amountAccount.toString() + item.typeMoney

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

                    binding.imgSelect.isActivated = item.select == true
                    binding.imgCategory.setImageResource(DataColor.showBackgroundColorCircle(context,
                        item.icon!!))
                    val color = DataColor.getIdColorById(item.color!!)
                    binding.imgCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(
                        context,
                        color.toString()))
                    binding.textNameCategory.text = item.nameAccount
                    binding.textValueCategory.text = item.amountAccount.toString() + item.typeMoney

                    binding.root.setOnClickListener {
                        for (i in listCategory.indices) {
                            listCategory[i].select = (i == position)
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

    fun updateData(newList: ArrayList<Account>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateSelectTransaction(idTransaction: Int) {
        for (i in listCategory) {
            i.select = (i.id == idTransaction)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Account) -> Unit)? = null

    private var clickLongItemSelect: ((Account) -> Unit)? = null

    fun setClickItemSelect(listener: (Account) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (Account) -> Unit) {
        clickLongItemSelect = listener
    }
}