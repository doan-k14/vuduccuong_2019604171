package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.*
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo

class AdapterNotification(
    var context: Context,
    var listNotification: List<NotificationInfo>,
) : RecyclerView.Adapter<AdapterNotification.ViewHolder>() {

    inner class ViewHolder(binding: ItemAdapterNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemAdapterNotificationBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAdapterNotificationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listNotification[position]

        with(holder) {

            binding.swNotification.isChecked = item.isNotificationSelected!!
            binding.textNameNotification.text = item.nameNotification

            binding.swNotification.setOnClickListener {
                listNotification[position].isNotificationSelected =
                    !listNotification[position].isNotificationSelected!!
                reloadData()
                clickItemSelectSw?.let {
                    it(item)
                }
            }

            binding.root.setOnClickListener {
                clickItemSelect?.let {
                    it(item)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return listNotification.size
    }

    fun updateData(newList: List<NotificationInfo>) {
        this.listNotification = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((NotificationInfo) -> Unit)? = null

    fun setClickItemSelect(listener: (NotificationInfo) -> Unit) {
        clickItemSelect = listener
    }

    private var clickItemSelectSw: ((NotificationInfo) -> Unit)? = null

    fun setClickItemSelectSw(listener: (NotificationInfo) -> Unit) {
        clickItemSelectSw = listener
    }

}