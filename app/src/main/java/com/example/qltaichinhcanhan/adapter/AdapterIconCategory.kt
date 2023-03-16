package com.example.qltaichinhcanhan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.IconCategoryData

class AdapterIconCategory(
    var context: Context,
    var listCategory: ArrayList<Category1>,
    var layoutType: LayoutType,
) : RecyclerView.Adapter<AdapterIconCategory.ViewHolder>() {

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
                binding = ItemIconCategoryBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                binding = ItemIconAddCategoryBinding.inflate(inflater, parent, false)
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
                    when (item.color) {
                        0 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_br)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.bg_color))
                        }
                        1 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_1)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color6))
                        }
                        2 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_2)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.yellow))
                        }
                        3 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_3)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color3))
                        }
                        4 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_4)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color4))
                        }
                        5 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_5)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color5))
                        }
                        6 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_6)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color6))
                        }
                        7 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_7)
                            binding.textNameCategory.setTextColor(ContextCompat.getColor(context,
                                R.color.color7))
                        }
                    }

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
                        binding.imgIcon.setBackgroundResource(R.drawable.color_icon_7)
                        binding.root.setBackgroundResource(R.drawable.custom_icon_while)
                    } else {
                        binding.imgIcon.setBackgroundResource(R.drawable.color_icon_br)
                        binding.root.background = null
                    }

                    when (item.color) {
                        0 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_br)
                        }
                        1 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_1)
                        }
                        2 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_2)
                        }
                        3 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_3)
                        }
                        4 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_4)
                        }
                        5 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_5)
                        }
                        6 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_6)
                        }
                        7 -> {
                            binding.imgIcon.setBackgroundResource(R.drawable.color_icon_7)
                        }
                    }

                    binding.root.setOnClickListener {
                        clickItemSelect?.let {
                            for (i in listCategory.indices) {
                                listCategory[i].select = (i == position)
                            }
                            notifyDataSetChanged()
                            it(item)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when (layoutType) {
            LayoutType.TYPE1 -> {
                listCategory.size
            }
            LayoutType.TYPE2 -> {
                6
            }
            else -> {
                listCategory.size
            }
        }
    }

    fun updateData(newList: ArrayList<Category1>) {
        this.listCategory = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Category1) -> Unit)? = null

    fun setClickItemSelect(listener: (Category1) -> Unit) {
        clickItemSelect = listener
    }


}