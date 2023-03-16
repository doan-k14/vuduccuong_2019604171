package com.example.qltaichinhcanhan.main.m

import android.content.Context
import com.example.qltaichinhcanhan.R

data class ImageCheckCircle(
    var id: Int,
    var idColor: Int,
    var color: Int,
    var select: Boolean? = null,
)

data class Color(
    var id: Int,
    var name: String,
    var color: Int,
)

data class BackgroundColorCircle(
    var id: Int,
    var name: String,
    var color: Int,
)

object DataColor {
    val listImageCheckCircle = arrayListOf<ImageCheckCircle>(
        ImageCheckCircle(0, 1, R.drawable.click_color_1, true),
        ImageCheckCircle(1, 2, R.drawable.click_color_2, false),
        ImageCheckCircle(2, 3, R.drawable.click_color_3, false),
        ImageCheckCircle(3, 4, R.drawable.click_color_4, false),
        ImageCheckCircle(4, 5, R.drawable.click_color_5, false),
        ImageCheckCircle(5, 6, R.drawable.click_color_6, false),
        ImageCheckCircle(6, 7, R.drawable.click_color_7, false)
    )

    val listColor = arrayListOf<Color>(
        Color(0, "bg_color", R.color.bg_color),
        Color(1, "color1", R.color.color1),
        Color(2, "color2", R.color.color2),
        Color(3, "color3", R.color.color3),
        Color(4, "color4", R.color.color4),
        Color(5, "color5", R.color.color5),
        Color(6, "color6", R.color.color6),
        Color(7, "color7", R.color.color7)
    )

    val listBackgroundColorCircle = arrayListOf<BackgroundColorCircle>(
        BackgroundColorCircle(0, "color_icon_br", R.drawable.color_icon_br),
        BackgroundColorCircle(1, "color_icon_1", R.drawable.color_icon_1),
        BackgroundColorCircle(2, "color_icon_2", R.drawable.color_icon_2),
        BackgroundColorCircle(3, "color_icon_3", R.drawable.color_icon_3),
        BackgroundColorCircle(4, "color_icon_4", R.drawable.color_icon_4),
        BackgroundColorCircle(5, "color_icon_5", R.drawable.color_icon_5),
        BackgroundColorCircle(6, "color_icon_6", R.drawable.color_icon_6),
        BackgroundColorCircle(7, "color_icon_7", R.drawable.color_icon_7)
    )

    fun showBackgroundColorCircle(context: Context, name: String): Int {
        val resources = context.resources
        return resources.getIdentifier(name, "drawable", context.packageName)
    }

    fun getIdColorById(id: Int): String? {
        val backgroundColorCircle = listBackgroundColorCircle.find { it.id == id }
        return backgroundColorCircle?.name
    }


}
