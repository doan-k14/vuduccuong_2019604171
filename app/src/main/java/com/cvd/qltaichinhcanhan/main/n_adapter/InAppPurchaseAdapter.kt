package com.cvd.qltaichinhcanhan.main.n_adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.cvd.qltaichinhcanhan.databinding.ItemInAppPurchaseBinding


class InAppPurchaseAdapter(
    private val context: Context,
    private var listMyImages: List<SkuDetails>
) :
    RecyclerView.Adapter<InAppPurchaseAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemInAppPurchaseBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ItemInAppPurchaseBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemInAppPurchaseBinding = ItemInAppPurchaseBinding.inflate(
            LayoutInflater.from(
                context
            ), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = listMyImages[position]
        Log.e("TAG", "queryProfileProducts: "+item.sku)

        holder.binding.productId.setText(item.sku)
        holder.binding.type.setText(item.type)
        holder.binding.title.setText(item.title)
        holder.binding.name.setText(item.description)
        holder.binding.iconUrl.setText(item.iconUrl)
        holder.binding.description.setText(item.description)
        holder.binding.price.setText(item.price)
        holder.binding.priceAmountMicros.setText(item.priceAmountMicros.toString() + "")
        holder.binding.priceCurrencyCode.setText(item.priceCurrencyCode)
        //        holder.binding.skuDetailsToken.setText(item.getOriginalJson());
        holder.binding.btnBuyNew.setOnClickListener(View.OnClickListener {
            itemClickListener!!.onItemClick(
                item
            )
        })
    }

    override fun getItemCount(): Int {
        return listMyImages.size
    }

    fun updateData(newList: List<SkuDetails>) {
        listMyImages = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(appVC: SkuDetails?)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }
}
