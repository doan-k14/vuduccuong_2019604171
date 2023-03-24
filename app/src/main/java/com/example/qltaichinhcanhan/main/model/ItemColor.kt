package com.example.qltaichinhcanhan.main.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.R


class ItemColor(val id: Int, val color: Int) {
    companion object {

        private val colors = arrayOf(
            ItemColor(1, R.color.color1),
            ItemColor(2, R.color.color2),
            ItemColor(3, R.color.color3),
            ItemColor(4, R.color.color4),
            ItemColor(5, R.color.color5),
            ItemColor(6, R.color.color6),
            ItemColor(7, R.color.color7)
        )
        fun getColors(context: Context): Array<Int> {
            val colorList = mutableListOf<Int>()
            colors.forEach { colorList.add(ContextCompat.getColor(context, it.color)) }
            return colorList.toTypedArray()
        }

        fun getColorForId(context: Context, id: Int): Int {
            return colors.firstOrNull { it.id == id }?.let {
                ContextCompat.getColor(context, it.color)
            } ?: throw IllegalArgumentException("No color found for id $id")
        }
    }
}
