package com.example.qltaichinhcanhan.main.model.m

import android.content.Context
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.model.Color
import com.example.qltaichinhcanhan.main.model.DataColor
import com.example.qltaichinhcanhan.main.model.ImageCheckCircle

data class IconR(
    var id: Int = 0,
    var iconName: String? = null,
    var type: Int? = null,
    var select: Boolean? = null,
    var idColorR: Int? = null,
) {
    companion object {
        val listIconRAccount = listOf<IconR>(
            IconR(1, "ic_account1", 1, false, 1),
            IconR(2, "ic_account2", 1, false, 1),
            IconR(3, "ic_account5", 1, false, 1),
            IconR(4, "ic_account6", 1, false, 1),
            IconR(5, "ic_account7", 1, false, 1),
            IconR(6, "ic_account8", 1, false, 1),
            IconR(7, "ic_account9", 1, false, 1),
            IconR(8, "ic_account10", 1, false, 1),
            IconR(9, "ic_account11", 1, false, 1),
            IconR(10, "ic_account12", 1, false, 1),
            IconR(11, "ic_account13", 1, false, 1),
            IconR(12, "ic_account14", 1, false, 1),
            IconR(13, "ic_account15", 1, false, 1),
            IconR(14, "ic_account16", 1, false, 1),
            IconR(15, "ic_account17", 1, false, 1),
            IconR(16, "ic_account18", 1, false, 1),
            IconR(17, "ic_account19", 1, false, 1),
            IconR(18, "ic_account20", 1, false, 1),
            IconR(19, "ic_account21", 1, false, 1)
        )
        val listIconRCategory = listOf<IconR>(
            IconR(1, "ic_add", 0, false, 3),
            IconR(2, "ic_more_horiz", 0, false, 1),
            IconR(3, "ic_ms2", 0, false, 2),
            IconR(4, "ic_ms3", 0, false, 4),
            IconR(5, "ic_gt", 0, false, 5),
            IconR(6, "ic_ms5", 0, false, 6),
            IconR(7, "ic_ms6", 0, false, 1),
            IconR(8, "ic_da", 0, false, 2),
            IconR(9, "ic_da1", 0, false, 7),
            IconR(10, "ic_ms5", 0, false, 3),
            IconR(11, "ic_sk", 0, false, 6)
        )
        // ở list edt thì cắt trong khoảng [] để trùng id thì hiện thị ( gộp tất cả về 1 list

        fun getListIconCheckCircle(): List<IconR> {
            return listOf(
                IconR(1, "click_color_1", 0, true, 1),
                IconR(2, "click_color_2", 0, false, 2),
                IconR(3, "click_color_3", 0, false, 3),
                IconR(4, "click_color_4", 0, false, 4),
                IconR(5, "click_color_5", 0, false, 5),
                IconR(6, "click_color_6", 0, false, 6),
                IconR(7, "click_color_7", 0, false, 7),
            )
        }

        fun getListColor() : List<ColorR>{
            return listOf(
                ColorR(0, "bg_color"),
                ColorR(1, "color1"),
                ColorR(2, "color2"),
                ColorR(3, "color3"),
                ColorR(4, "color4"),
                ColorR(5, "color5"),
                ColorR(6, "color6"),
                ColorR(7, "color7"),
            )
        }

        fun getListColorIconR() : List<ColorR>{
            return listOf(
                ColorR(0, "color_icon_br"),
                ColorR(1, "color_icon_1"),
                ColorR(2, "color_icon_2"),
                ColorR(3, "color_icon_3"),
                ColorR(4, "color_icon_4"),
                ColorR(5, "color_icon_5"),
                ColorR(6, "color_icon_6"),
                ColorR(7, "color_icon_7"),
            )
        }




        fun showBackgroundColorCircle(context: Context, name: String): Int {
            val resources = context.resources
            return resources.getIdentifier(name, "drawable", context.packageName)
        }

        fun getIconById(context: Context, id: Int,listIconR:List<IconR>): Int {
            val urlIconR = listIconR.find { it.id == id }
            val resources = context.resources
            return resources.getIdentifier(urlIconR!!.iconName, "drawable", context.packageName)
        }


        fun getColorById(context: Context, id: Int,listIconR:List<ColorR>): Int {
            val urlIconR = listIconR.find { it.id == id }
            val resources = context.resources
            return resources.getIdentifier(urlIconR!!.colorName, "drawable", context.packageName)
        }


    }

}