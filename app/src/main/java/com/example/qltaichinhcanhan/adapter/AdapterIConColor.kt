package com.example.qltaichinhcanhan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemColorBinding
import com.example.qltaichinhcanhan.main.m.ImageCheckCircle

class AdapterIConColor(
    var context: Context,
    var listIConColor: ArrayList<ImageCheckCircle>,
) : RecyclerView.Adapter<AdapterIConColor.ViewHolder>() {
    private var selectedItem: ImageCheckCircle? = null

    inner class ViewHolder(binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemColorBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listIConColor[position]
        with(holder) {
            binding.imgIconColor.isActivated = item.select == true
            binding.imgIconColor.setImageResource(item.color)

            binding.root.setOnClickListener {
                clickItemSelect?.let {
                    for (i in listIConColor.indices) {
                        listIConColor[i].select = (i == position)
                    }
                    notifyDataSetChanged()
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listIConColor.size
    }

    fun updateData(newList: ArrayList<ImageCheckCircle>) {
        this.listIConColor = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }

    private var clickItemSelect: ((ImageCheckCircle) -> Unit)? = null

    fun setClickItemSelect(listener: (ImageCheckCircle) -> Unit) {
        clickItemSelect = listener
    }
}