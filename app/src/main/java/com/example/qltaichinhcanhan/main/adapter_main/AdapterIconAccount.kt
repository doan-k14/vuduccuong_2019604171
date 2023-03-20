package com.example.qltaichinhcanhan.main.adapter_main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.main.m.*

class AdapterIconAccount(
    var context: Context,
    var listCategory: ArrayList<IconAccount>,
) : RecyclerView.Adapter<AdapterIconAccount.ViewHolder>() {


    inner class ViewHolder(binding: ItemIconAddCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemIconAddCategoryBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterIconAccount.ViewHolder {
        val binding =
            ItemIconAddCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        with(holder) {
            val resources = context.resources
            val imageResourceId =
                resources.getIdentifier(item.name, "drawable", context.packageName)
            binding.imgIcon.setImageResource(imageResourceId)

            if (item.select == true) {
                binding.imgIcon.setBackgroundResource(DataColor.setCustomBackgroundColorCircleById(
                    context,
                    item.color!!))
                binding.root.setBackgroundResource(R.drawable.custom_icon_while)
            } else {
                binding.imgIcon.setBackgroundResource(R.drawable.color_icon_br)
                binding.root.background = null
            }

            binding.root.setOnClickListener {
                clickItemSelect?.let {
                    for (i in 0 until listCategory.size - 1) {
                        listCategory[i].select = (i == position)
                    }
                    notifyDataSetChanged()
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun updateData(newList: ArrayList<IconAccount>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateColor(idColor: Int) {
        for (i in this.listCategory) {
            i.color = idColor
        }
        reloadData()
    }
    fun updateSelect(name:String){
        for (i in listCategory) {
            i.select = (i.name == name)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((IconAccount) -> Unit)? = null

    fun setClickItemSelect(listener: (IconAccount) -> Unit) {
        clickItemSelect = listener
    }
}