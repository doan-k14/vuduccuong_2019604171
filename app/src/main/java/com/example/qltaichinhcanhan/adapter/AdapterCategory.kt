package com.example.qltaichinhcanhan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemCategoryBinding
import com.example.qltaichinhcanhan.mode.Category

class AdapterCategory(
    var context: Context,
    var listCategory: ArrayList<Category>,
) : RecyclerView.Adapter<AdapterCategory.ViewHolder>() {

    inner class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemCategoryBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        with(holder) {
            binding.imgCategory.isActivated = item.select == true
            binding.textNameCategory.text = item.name

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

    fun updateData(newList: ArrayList<Category>) {
        this.listCategory = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Category) -> Unit)? = null

    private var clickLongItemSelect: ((Category) -> Unit)? = null

    fun setClickItemSelect(listener: (Category) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (Category) -> Unit) {
        clickLongItemSelect = listener
    }
}