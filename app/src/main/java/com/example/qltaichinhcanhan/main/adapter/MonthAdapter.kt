package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ItemMonthBinding
import com.example.qltaichinhcanhan.main.model.m.DefaultData

class MonthAdapter(private val context: Context, private var months: List<DefaultData.MothR>) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {


    inner class ViewHolder(binding: ItemMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemMonthBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemMonthBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = months[position]
        with(holder) {
            if (item.selectMoth == true) {
                binding.root.setBackgroundResource(R.drawable.button_save_category)
            } else {
                binding.root.background = null
            }
            binding.monthName.text = item.mothName
            binding.root.setOnClickListener {
                for (i in months.indices) {
                    months[i].selectMoth = (i == position)
                }
                reloadData()
                clickItemSelect?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return months.size
    }

    fun updateData(newList: List<DefaultData.MothR>) {
        this.months = newList
        reloadData()
    }


    fun updateSelect(idIcon: Int) {
        for (i in months) {
            i.selectMoth = (i.idMothR == idIcon)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((DefaultData.MothR) -> Unit)? = null

    fun setClickItemSelect(listener: (DefaultData.MothR) -> Unit) {
        clickItemSelect = listener
    }
}
