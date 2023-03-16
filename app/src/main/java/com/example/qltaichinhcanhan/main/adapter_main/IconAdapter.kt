package com.example.qltaichinhcanhan.main.adapter_main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.inf.IconClickListener
import com.example.qltaichinhcanhan.main.m.Icon

class IconAdapter(private val icons: List<Icon>, private val iconClickListener: IconClickListener) :
    RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImage: ImageView = itemView.findViewById(R.id.icon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = icons[position]
        holder.iconImage.setImageResource(icon.url)
        holder.iconImage.setColorFilter(icon.color)

        holder.itemView.setOnClickListener {
            iconClickListener.onIconClick(icon)
        }
    }

    override fun getItemCount() = icons.size
}

