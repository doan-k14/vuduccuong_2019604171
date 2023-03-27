package com.example.qltaichinhcanhan.splash.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.ItemColorBinding
import com.example.qltaichinhcanhan.main.model.ImageCheckCircle
import com.example.qltaichinhcanhan.main.model.m.IconR

class AdapterIConColor(
    var context: Context,
    var listIConColor: List<IconR>,
) : RecyclerView.Adapter<AdapterIConColor.ViewHolder>() {
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
            binding.imgIconColor.setImageResource(IconR.getIconById(context,
                item.id,
                IconR.getListIconCheckCircle()))

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

    fun updateData(newList: List<IconR>) {
        this.listIConColor = newList
        reloadData()
    }

    fun updateSelectColor(idColor: Int) {
        for (i in listIConColor) {
            i.select = (i.idColorR == idColor)
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