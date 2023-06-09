package com.example.qltaichinhcanhan.splash.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.Category
import com.example.qltaichinhcanhan.main.model.DataColor
import androidx.core.content.ContextCompat

class AdapterIconCategory(
    var context: Context,
    var listCategory: ArrayList<Category>,
    var layoutType: LayoutType,
) : RecyclerView.Adapter<AdapterIconCategory.ViewHolder>() {

    enum class LayoutType {
        TYPE1,
        TYPE2,
        TYPE3
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
                binding = ItemIconCategoryBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                binding = ItemIconAddCategoryBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE3 -> {
                binding = ItemIconCategoryBinding.inflate(inflater, parent, false)
            }
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]
        with(holder) {
            when (layoutType) {
                LayoutType.TYPE1 -> {
                    binding as ItemIconCategoryBinding
                    val resources = context.resources
                    val imageResourceId =
                        resources.getIdentifier(item.icon, "drawable", context.packageName)
                    binding.imgIcon.setImageResource(imageResourceId)

                    binding.textNameCategory.text = item.nameCategory
                    binding.imgIcon.setBackgroundResource(DataColor.setCustomBackgroundColorCircleById(
                        context,
                        item.color!!))
                    binding.textNameCategory.setTextColor(ContextCompat.getColor(binding.textNameCategory.context,
                        DataColor.setColorById(item.color!!)))

                    binding.root.setOnClickListener {
                        clickItemSelect?.let {
                            it(item)
                        }
                    }

                }
                LayoutType.TYPE2 -> {
                    binding as ItemIconAddCategoryBinding
                    val resources = context.resources
                    val imageResourceId =
                        resources.getIdentifier(item.icon, "drawable", context.packageName)
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
                    if (item.id == 1) {
                        binding.imgIcon.setBackgroundResource(R.drawable.color_icon_2)
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

                LayoutType.TYPE3 -> {
                    binding as ItemIconCategoryBinding
                    val resources = context.resources
                    val imageResourceId =
                        resources.getIdentifier(item.icon, "drawable", context.packageName)
                    binding.imgIcon.setImageResource(imageResourceId)

                    binding.textNameCategory.text = item.nameCategory
                    binding.imgIcon.setBackgroundResource(DataColor.setCustomBackgroundColorCircleById(context, item.color!!))
                    binding.textNameCategory.setTextColor(ContextCompat.getColor(binding.textNameCategory.context,
                        DataColor.setColorById(item.color!!)))

                    if (item.select == true) {
                        binding.root.setBackgroundResource(R.drawable.custom_icon_while)
                    } else {
                        binding.root.background = null
                    }

                    binding.root.setOnClickListener {
                        for (i in 0 until listCategory.size - 1) {
                            listCategory[i].select = (i == position)
                        }
                        notifyDataSetChanged()
                        clickItemSelect?.let {
                            it(item)
                        }
                    }

                }
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

    fun updateColor(idColor: Int) {
        for (i in this.listCategory) {
            i.color = idColor
        }
        reloadData()
    }

    fun updateSelect(idSelect:Int){
        for (i in listCategory) {
            i.select = (i.id == idSelect)
        }
        reloadData()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Category) -> Unit)? = null

    fun setClickItemSelect(listener: (Category) -> Unit) {
        clickItemSelect = listener
    }
}