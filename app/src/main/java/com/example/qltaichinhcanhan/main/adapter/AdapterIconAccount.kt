package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.m.IconR

class AdapterIconAccount(
    var context: Context,
    var listCategory: List<IconR>,
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
            binding.imgIcon.setImageResource(IconR.getIconById(context,item.id,IconR.listIconRAccount))
            if (item.select == true) {
                binding.imgIcon.setBackgroundResource(IconR.getColorById(context, item.idColorR!!, IconR.getListColorIconR()))
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

    fun updateData(newList: ArrayList<IconR>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateColor(idColor: Int) {
        for (i in this.listCategory) {
            i.idColorR = idColor
        }
        reloadData()
    }

    fun updateSelect(idIcon: Int) {
        for (i in listCategory) {
            i.select = (i.id == idIcon)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((IconR) -> Unit)? = null

    fun setClickItemSelect(listener: (IconR) -> Unit) {
        clickItemSelect = listener
    }
}