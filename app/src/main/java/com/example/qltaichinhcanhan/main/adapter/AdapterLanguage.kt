package com.example.qltaichinhcanhan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.*
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m.LanguageR

class AdapterLanguage(
    var context: Context,
    var listLanguageR: List<LanguageR>,
) : RecyclerView.Adapter<AdapterLanguage.ViewHolder>() {


    inner class ViewHolder(binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemLanguageBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterLanguage.ViewHolder {
        val binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLanguageR[position]
        with(holder) {

            binding.imgLanguage.setImageDrawable(context.getDrawable(item.flagLanguage!!))

            binding.textNameLanguage.text = context.getString(item.nameLanguageR!!)
            binding.textNameLanguageConvert.text = context.getString(item.nameLanguageR1!!)
            binding.line.visibility = View.VISIBLE
            if (item.idLanguageR == 9) {
                binding.line.visibility = View.GONE
            }
            if (item.selectLanguage == true) {
                binding.imgSelect.visibility = View.VISIBLE
            } else {
                binding.imgSelect.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                for (i in listLanguageR.indices) {
                    listLanguageR[i].selectLanguage = (i == position)
                }
                reloadData()
                clickItemSelect?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listLanguageR.size
    }

    fun updateData(newList: List<LanguageR>) {
        this.listLanguageR = newList
        reloadData()
    }

    fun updateSelect(codeLanguageR: String) {
        for (i in listLanguageR) {
            i.selectLanguage = (i.codeLanguage == codeLanguageR)
        }
        reloadData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reloadData() {
        notifyDataSetChanged()
    }


    private var clickItemSelect: ((LanguageR) -> Unit)? = null

    fun setClickItemSelect(listener: (LanguageR) -> Unit) {
        clickItemSelect = listener
    }
}