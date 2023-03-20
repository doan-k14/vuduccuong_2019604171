package com.example.qltaichinhcanhan.main.adapter_main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.m.*

class AdapterCountry(
    var context: Context,
    var listCategory: ArrayList<Country>,
) : RecyclerView.Adapter<AdapterCountry.ViewHolder>() {

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
//            binding.imgCategory.setImageResource(DataColor.showBackgroundColorCircle(context,
//                item.icon!!))
//            val color = DataColor.getIdColorById(item.color!!)
//            binding.imgCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(context,
//                color.toString()))

            binding.textNameCategory.text = item.name
            binding.textValueCategory.text = item.currencyCode


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

    fun updateData(newList: ArrayList<Country>) {
        this.listCategory = newList
        reloadData()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Country) -> Unit)? = null

    private var clickLongItemSelect: ((Country) -> Unit)? = null

    fun setClickItemSelect(listener: (Country) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (Country) -> Unit) {
        clickLongItemSelect = listener
    }
}