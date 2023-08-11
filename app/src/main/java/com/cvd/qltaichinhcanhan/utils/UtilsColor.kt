package com.cvd.qltaichinhcanhan.utils

import android.content.Context
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.model.DataColor
import com.cvd.qltaichinhcanhan.main.model.m.IconR

object UtilsColor {

    fun setImageByName(context: Context, iconName: String): Int {
        val resources = context.resources
        return resources.getIdentifier(iconName, "drawable", context.packageName)
    }
    fun setBackgroundCircleCategoryById(context: Context, id: Int): Int {
        var stringColor = ""
        when (id) {
            0 -> stringColor = "color_icon_br"
            1 -> stringColor = "color_icon_1"
            2 -> stringColor = "color_icon_2"
            3 -> stringColor = "color_icon_3"
            4 -> stringColor = "color_icon_4"
            5 -> stringColor = "color_icon_5"
            6 -> stringColor = "color_icon_6"
            7 -> stringColor = "color_icon_7"
            8 -> stringColor = "color_icon_1"
        }
        val resources = context.resources
        return resources.getIdentifier(stringColor, "drawable", context.packageName)
    }

    fun setTextColor(id: Int): Int {
        var intColor = 0
        when (id) {
            0 -> intColor = R.color.bg_color
            1 -> intColor = R.color.color1
            2 -> intColor = R.color.color2
            3 -> intColor = R.color.color3
            4 -> intColor = R.color.color4
            5 -> intColor = R.color.color5
            6 -> intColor = R.color.color6
            7 -> intColor = R.color.color7
            8 -> intColor = R.color.color1
        }
       return intColor
    }

    // select check

    fun setCheckCircleById(context: Context, id: Int): Int {
        var stringCheckCircle = ""
        when (id) {
            1 -> stringCheckCircle = "click_color_1"
            2 -> stringCheckCircle = "click_color_2"
            3 -> stringCheckCircle = "click_color_3"
            4 -> stringCheckCircle = "click_color_4"
            5 -> stringCheckCircle = "click_color_5"
            6 -> stringCheckCircle = "click_color_6"
            7 -> stringCheckCircle = "click_color_7"
            8 -> stringCheckCircle = "click_color_8"
        }
        val resources = context.resources
        return resources.getIdentifier(stringCheckCircle, "drawable", context.packageName)
    }

}