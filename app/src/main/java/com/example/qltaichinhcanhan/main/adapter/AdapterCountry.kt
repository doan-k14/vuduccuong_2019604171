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

class AdapterCountry(
    var context: Context,
    var listCategory: ArrayList<Country>,
    var nameCountry: String? = null,
) : RecyclerView.Adapter<AdapterCountry.ViewHolder>() {

    inner class ViewHolder(binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemCountryBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]


        with(holder) {
            var pngUrl = ""
            pngUrl = if (item.id == 1) {
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_the_Taliban.svg/320px-Flag_of_the_Taliban.svg.png"
            } else {
                val svgUrl = item.flagUrl.toString()
                svgUrl.replace(".svg", ".png").replace("flagcdn.com", "flagcdn.com/w320")
            }
            Glide.with(context)
                .load(pngUrl)
                .placeholder(R.drawable.ic_error_b)
                .error(R.drawable.ic_picturel_andscape)
                .transform(RoundedCorners(16))
                .into(binding.imgCategory)

            if (item.select == true) {
                binding.root.setBackgroundResource(R.drawable.button_delete_category)
            } else {
                binding.root.setBackgroundResource(R.drawable.custom_button_white)
            }

            binding.textNameCategory.text = item.name + " - " + item.exchangeRate.toString()
            binding.textValueCategory.text = item.currencyCode


            binding.root.setOnClickListener {
                clickItemSelect?.let {
                    it(item)
                }
            }

            binding.root.setOnLongClickListener {
                clickLongItemSelect?.let {
                    it(item)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun updateData(newList: ArrayList<Country>) {
        this.listCategory = newList
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((Country) -> Unit)? = null

    private var clickLongItemSelect: ((Country) -> Unit)? = null

    fun setClickItemSelect(listener: (Country) -> Unit) {
        clickItemSelect = listener
    }

    fun setClickLongItemSelect(listener: (Country) -> Unit) {
        clickLongItemSelect = listener
    }
}