package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.*
import com.example.qltaichinhcanhan.main.model.m_r.Country

class AdapterCountry(
    var context: Context,
    var listCategory: ArrayList<Country>,
    var layoutType: LayoutType,
    var countryDefault: Country? = null,
    var countryNew: Country? = null,
    var money: Float? = null,
) : RecyclerView.Adapter<AdapterCountry.ViewHolder>() {

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
                binding = ItemCountryBinding.inflate(inflater, parent, false)
            }
            LayoutType.TYPE2 -> {
                binding = ItemCountryConversionBinding.inflate(inflater, parent, false)
            }
        }

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCategory[position]

        with(holder) {
            when (layoutType) {
                LayoutType.TYPE1 -> {
                    binding as ItemCountryBinding
                    var pngUrl = ""
                    pngUrl = if (item.currencyCode == "AFN") {
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

                    if (item.selectCountry == true) {
                        binding.root.setBackgroundResource(R.drawable.button_delete_category)
                    } else {
                        binding.root.setBackgroundResource(R.drawable.custom_button_white)
                    }

                    binding.textNameCategory.text =
                        item.countryName
//                    + " - " + item.exchangeRate.toString()
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
                LayoutType.TYPE2 -> {
                    binding as ItemCountryConversionBinding
                    var pngUrl = ""
                    pngUrl = if (item.currencyCode == "AFN") {
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_the_Taliban.svg/320px-Flag_of_the_Taliban.svg.png"
                    } else {
                        val svgUrl = item.flagUrl.toString()
                        svgUrl.replace(".svg", ".png").replace("flagcdn.com", "flagcdn.com/w320")
                    }
                    Glide.with(context)
                        .load(pngUrl)
                        .placeholder(R.drawable.ic_error_b)
                        .error(R.drawable.ic_picturel_andscape)
                        .into(binding.imgFlagUrl)

                    binding.textCurrencySymbol.text = item.currencySymbol
                    binding.textCurrencyCode.text = item.currencyCode
                    if (countryDefault!!.idCountry == countryNew!!.idCountry) {
                        if (money != 0F) {
                            binding.textAmount.text = (money!! * item.exchangeRate!!).toString()
                        }
                        binding.textExchangeRate.text =
                            "1 ${countryDefault!!.currencyCode} ≈ ${item.exchangeRate!!.toString()} ${item.currencyCode}"
                    } else {
                        val m = converexchangeRate(countryDefault?.exchangeRate!!,
                            countryNew!!.exchangeRate!!,
                            item.exchangeRate!!)
                        if (money != 0F) {
                            binding.textAmount.text = (money!! / m).toString()
                        }
                        binding.textExchangeRate.text =
                            "1 ${countryNew!!.currencyCode} ≈ ${1 / m} ${item.currencyCode}"
                    }
                }
            }


        }
    }

    fun converexchangeRate(default: Float, new: Float, item: Float): Float {
        return (default / item) * (1 / (default / new))
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    fun updateData(newList: ArrayList<Country>) {
        this.listCategory = newList
        reloadData()
    }

    fun updateMoney(moneyNew: Float) {
        this.money = moneyNew
        reloadData()
    }

    fun updateCountryNew(countryNew: Country) {
        this.countryNew = countryNew
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