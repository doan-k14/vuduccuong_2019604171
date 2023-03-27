package com.example.qltaichinhcanhan.main.model

import android.content.Context
import com.example.qltaichinhcanhan.R


data class Icon(val id: Int?=null, var name: String?=null, val url: Int?=null, var color: Int?=null)
data class IconAccount(var id: Int?=null, var name: String?=null, val url: Int?=null, var color: Int?=null, var select:Boolean?=null)

data class IconCategory(val id: Int, val name: String, val icons: List<Icon>)

object IconCategoryData {
    val iconList = listOf(
        Icon(1, "ic_add", R.drawable.ic_add, 1),
        Icon(2, "ic_ms1", R.drawable.ic_ms1, 1),
        Icon(3, "ic_ms2", R.drawable.ic_ms2, 1),
        Icon(4, "ic_ms3", R.drawable.ic_ms3, 1),
        Icon(5, "ic_ms4", R.drawable.ic_ms4, 1),
        Icon(6, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(7, "ic_sk", R.drawable.ic_sk, 1)
    )

    private val categoryList = listOf(
        IconCategory(1, "Thể thao", iconList),
        IconCategory(2, "Giải trí", iconList)
    )

    fun getIconCategoryList(): List<IconCategory> = categoryList

    fun showICon(context: Context, name: String): Int {
        val resources = context.resources
        return resources.getIdentifier(name, "drawable", context.packageName)
    }

    val iconList1 = listOf(
        Icon(2, "ic_ms1", R.drawable.ic_ms1, 1),
        Icon(3, "ic_ms2", R.drawable.ic_ms2, 1),
        Icon(4, "ic_ms3", R.drawable.ic_ms3, 1),
        Icon(5, "ic_ms4", R.drawable.ic_ms4, 1),
        Icon(6, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(7, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(8, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(9, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(10, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(11, "ic_ms5", R.drawable.ic_ms5, 1),
        Icon(12, "ic_sk", R.drawable.ic_sk, 1)
    )

    private val categoryList1 = listOf(
        IconCategory(1, "Thể thao", iconList1),
        IconCategory(2, "Sức khỏe", iconList1),
        IconCategory(3, "Mua sắm", iconList1),
        IconCategory(4, "Thể thao", iconList1),
        IconCategory(5, "Thể thao", iconList1),
        IconCategory(6, "Thể thao", iconList1),
        IconCategory(7, "Thể thao", iconList1),
        IconCategory(8, "Thể thao", iconList1),
        IconCategory(9, "Thể thao", iconList1),
        IconCategory(10, "Giải trí", iconList1)
    )

    fun getIconCategoryList1(): List<IconCategory> = categoryList1
}
