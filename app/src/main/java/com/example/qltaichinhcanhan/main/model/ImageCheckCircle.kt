package com.example.qltaichinhcanhan.main.model

import android.content.Context
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount

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
    var nameBackground: String,
    var nameColor: String,
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
        BackgroundColorCircle(0, "color_icon_br", "bg_color", R.drawable.color_icon_br),
        BackgroundColorCircle(1, "color_icon_1", "color1", R.drawable.color_icon_1),
        BackgroundColorCircle(2, "color_icon_2", "color2", R.drawable.color_icon_2),
        BackgroundColorCircle(3, "color_icon_3", "color3", R.drawable.color_icon_3),
        BackgroundColorCircle(4, "color_icon_4", "color4", R.drawable.color_icon_4),
        BackgroundColorCircle(5, "color_icon_5", "color5", R.drawable.color_icon_5),
        BackgroundColorCircle(6, "color_icon_6", "color6", R.drawable.color_icon_6),
        BackgroundColorCircle(7, "color_icon_7", "color7", R.drawable.color_icon_7)
    )

    fun showBackgroundColorCircle(context: Context, name: String): Int {
        val resources = context.resources
        return resources.getIdentifier(name, "drawable", context.packageName)
    }

    fun getIdColorById(id: Int): String? {
        val backgroundColorCircle = listBackgroundColorCircle.find { it.id == id }
        return backgroundColorCircle?.nameBackground
    }


    fun setCustomBackgroundColorCircleById(context: Context, id: Int): Int {
        val backgroundColorCircle = listBackgroundColorCircle.find { it.id == id }
        val resources = context.resources
        return resources.getIdentifier(backgroundColorCircle!!.nameBackground,
            "drawable",
            context.packageName)
    }

    fun setColorById(id: Int): Int {
        val color = listColor.find { it.id == id }
        return color!!.color
    }

//    val listIcon = IconCategoryData.iconList
//    private var listCategory = arrayListOf<Category>(
//        Category(2, "category1", 1, 1F, listIcon[1].name, 1, false),
//        Category(3, "category2", 1, 1F, listIcon[2].name, 1, false),
//        Category(4, "category3", 1, 1F, listIcon[3].name, 1, false),
//        Category(5, "category4", 1, 1F, listIcon[4].name, 1, false),
//        Category(6, "category5", 1, 1F, listIcon[4].name, 1, false),
//        Category(1, "Thêm", 1, 1F, listIcon[0].name, 2, false),
//    )
//


    fun getListCheckCircle(): ArrayList<ImageCheckCircle> {
        return arrayListOf(
            ImageCheckCircle(0, 1, R.drawable.click_color_1, true),
            ImageCheckCircle(1, 2, R.drawable.click_color_2, false),
            ImageCheckCircle(2, 3, R.drawable.click_color_3, false),
            ImageCheckCircle(3, 4, R.drawable.click_color_4, false),
            ImageCheckCircle(4, 5, R.drawable.click_color_5, false),
            ImageCheckCircle(5, 6, R.drawable.click_color_6, false),
            ImageCheckCircle(6, 7, R.drawable.click_color_7, false)
        )
    }


    fun getListIconAccount(): ArrayList<IconAccount> {
        return arrayListOf(
            IconAccount(1, "ic_account1", R.drawable.ic_account1, 1, false),
            IconAccount(2, "ic_account2", R.drawable.ic_account2, 1, false),
            IconAccount(5, "ic_account5", R.drawable.ic_account5, 1, false),
            IconAccount(6, "ic_account6", R.drawable.ic_account6, 1, false),
            IconAccount(7, "ic_account7", R.drawable.ic_account7, 1, false),
            IconAccount(8, "ic_account8", R.drawable.ic_account8, 1, false),
            IconAccount(9, "ic_account9", R.drawable.ic_account9, 1, false),
            IconAccount(10, "ic_account10", R.drawable.ic_account10, 1, false),
            IconAccount(11, "ic_account11", R.drawable.ic_account11, 1, false),
            IconAccount(12, "ic_account12", R.drawable.ic_account12, 1, false),
            IconAccount(13, "ic_account13", R.drawable.ic_account13, 1, false),
            IconAccount(14, "ic_account14", R.drawable.ic_account14, 1, false),
            IconAccount(15, "ic_account15", R.drawable.ic_account15, 1, false),
            IconAccount(16, "ic_account16", R.drawable.ic_account16, 1, false),
            IconAccount(17, "ic_account17", R.drawable.ic_account17, 1, false),
            IconAccount(18, "ic_account18", R.drawable.ic_account18, 1, false),
            IconAccount(19, "ic_account19", R.drawable.ic_account19, 1, false),
            IconAccount(20, "ic_account20", R.drawable.ic_account20, 1, false),
            IconAccount(21, "ic_account21", R.drawable.ic_account21, 1, false)
        )
    }

}
