package com.example.qltaichinhcanhan.main.adapter_main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.inf.IconClickListener
import com.example.qltaichinhcanhan.main.m.IconCategory

class IconCategoryAdapter(
    var context: Context,
    private val categories: List<IconCategory>,private val iconClickListener: IconClickListener
) : RecyclerView.Adapter<IconCategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val iconList: RecyclerView = itemView.findViewById(R.id.icon_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        val layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        holder.iconList.layoutManager = layoutManager
        holder.iconList.adapter = IconAdapter(category.icons,iconClickListener)
    }

    override fun getItemCount() = categories.size
}
