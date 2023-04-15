package com.example.qltaichinhcanhan.main.model.m

import android.content.Context
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.model.Icon
import com.example.qltaichinhcanhan.main.model.IconCategory
import com.example.qltaichinhcanhan.main.model.IconCategoryData
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo

object DefaultData {

    fun getListCategoryAdd(): ArrayList<Category> {
        return arrayListOf(
            Category(2, "category2", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[2].id,1,1),
            Category(3, "category3", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[3].id,1,1),
            Category(4, "category4", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[4].id,1,1),
            Category(5, "category5", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[5].id,1,1),
            Category(6, "category6", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[6].id,1,1),
            Category(1, "Thêm", CategoryType.EXPENSE, 0F, false, IconR.listIconRCategory[1].id,1,1),
        )
    }

    fun getListCategoryCreateData(context: Context): ArrayList<Category> {
        val listIcon = IconR.listIconRCategory
        return arrayListOf(
            Category(1, context.resources.getString(R.string.add), CategoryType.INCOME, 0F, false,listIcon[0].id,listIcon[0].idColorR,1),
            Category(2, context.resources.getString(R.string.add), CategoryType.EXPENSE, 0F, false,listIcon[0].id,listIcon[0].idColorR,1),
            Category(3, context.resources.getString(R.string.family), CategoryType.EXPENSE, 0F, false,listIcon[2].id,listIcon[2].idColorR,1),
            Category(4, context.resources.getString(R.string.move), CategoryType.EXPENSE, 0F, false,listIcon[3].id,listIcon[3].idColorR,1),
            Category(5, context.resources.getString(R.string.health), CategoryType.EXPENSE, 0F, false,listIcon[4].id,listIcon[4].idColorR,1),
            Category(6, context.resources.getString(R.string.other), CategoryType.EXPENSE, 0F, false,listIcon[5].id,listIcon[5].idColorR,1),
            Category(7, context.resources.getString(R.string.wage), CategoryType.INCOME, 0F, false,listIcon[6].id,listIcon[6].idColorR,1),
            Category(8, context.resources.getString(R.string.bonus), CategoryType.INCOME, 0F, false,listIcon[3].id,listIcon[1].idColorR,1),
            Category(9, context.resources.getString(R.string.invest), CategoryType.INCOME, 0F, false,listIcon[7].id,listIcon[7].idColorR,1),
            Category(10, context.resources.getString(R.string.other), CategoryType.INCOME, 0F, false,listIcon[8].id,listIcon[8].idColorR,1),
        )
    }

    data class IconRCategory(val id: Int, val name: String, val icons: List<IconR>)


    val listIconRCategory = listOf(
        IconRCategory(1, "Thể thao", IconR.listIconRCategory),
        IconRCategory(2, "Sức khỏe", IconR.listIconRCategory),
        IconRCategory(3, "Mua sắm", IconR.listIconRCategory),
        IconRCategory(4, "Thể thao", IconR.listIconRCategory),
        IconRCategory(5, "Thể thao", IconR.listIconRCategory),
        IconRCategory(6, "Thể thao", IconR.listIconRCategory),
        IconRCategory(7, "Thể thao", IconR.listIconRCategory),
        IconRCategory(8, "Thể thao", IconR.listIconRCategory),
        IconRCategory(9, "Thể thao", IconR.listIconRCategory),
        IconRCategory(10, "Giải trí", IconR.listIconRCategory),
    )


    data class MothR(
        var idMothR:Int?=null,
        var mothName: String? = null,
        var selectMoth: Boolean? = null,
    )

    val listMothR = listOf(
        MothR(1,"Tháng 1", false),
        MothR(2,"Tháng 2", false),
        MothR(3,"Tháng 3", false),
        MothR(4,"Tháng 4", false),
        MothR(5,"Tháng 5", false),
        MothR(6,"Tháng 6", false),
        MothR(7,"Tháng 7", false),
        MothR(8,"Tháng 8", false),
        MothR(9,"Tháng 9", false),
        MothR(10,"Tháng 10", false),
        MothR(11,"Tháng 11", false),
        MothR(12,"Tháng 12", false)
        )

    val headerFileXlsAndCSV = arrayOf(
        "Ngày tháng",
        "Danh mục",
        "Tài khoản",
        "Số tiền mặc định",
        "Loại tiền mặc định",
        "Số tiền theo tài khoản",
        "Loại tiền theo tài khoản",
        "Ghi chú"
    )

}