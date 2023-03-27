package com.example.qltaichinhcanhan.main.model.m

import com.example.qltaichinhcanhan.main.model.Icon
import com.example.qltaichinhcanhan.main.model.IconCategory
import com.example.qltaichinhcanhan.main.model.IconCategoryData
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType

object DefaultData {

    fun getListCategoryAdd(): ArrayList<Category> {
        return arrayListOf(
            Category(2, "category2", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[2].id,1,1),
            Category(3, "category3", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[3].id,1,1),
            Category(4, "category4", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[4].id,1,1),
            Category(5, "category5", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[5].id,1,1),
            Category(6, "category6", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[6].id,1,1),
            Category(1, "Thêm", CategoryType.EXPENSE, 1F, false, IconR.listIconRCategory[1].id,1,1),
        )
    }

    fun getListCategoryCreateData():ArrayList<Category>{
        val listIcon = IconR.listIconRCategory
        return arrayListOf(
            Category(1, "Thêm", CategoryType.INCOME, 1F, false,listIcon[0].id,listIcon[0].idColorR,1),
            Category(2, "Thêm", CategoryType.EXPENSE, 1F, false,listIcon[0].id,listIcon[0].idColorR,1),
            Category(3, "Gia đình", CategoryType.EXPENSE, 1F, false,listIcon[2].id,listIcon[2].idColorR,1),
            Category(4, "Di chuyển", CategoryType.EXPENSE, 1F, false,listIcon[3].id,listIcon[3].idColorR,1),
            Category(5, "Sức khỏe", CategoryType.EXPENSE, 1F, false,listIcon[4].id,listIcon[4].idColorR,1),
            Category(6, "Khác", CategoryType.EXPENSE, 1F, false,listIcon[5].id,listIcon[5].idColorR,1),
            Category(7, "Lương", CategoryType.INCOME, 1F, false,listIcon[6].id,listIcon[6].idColorR,1),
            Category(8, "Thưởng", CategoryType.INCOME, 1F, false,listIcon[1].id,listIcon[1].idColorR,1),
            Category(9, "Đầu tư", CategoryType.INCOME, 1F, false,listIcon[7].id,listIcon[7].idColorR,1),
            Category(10, "Khác", CategoryType.INCOME, 1F, false,listIcon[8].id,listIcon[8].idColorR,1),
        )
    }

    data class IconRCategory(val id: Int, val name: String, val icons: List<IconR>)


    val listIconRCategory = listOf(
        IconRCategory(1, "Thể thao", IconR.listIconRCategory),
        IconRCategory(2, "Sức khỏe", IconR.listIconRCategory),
        IconRCategory(3, "Mua sắm",IconR.listIconRCategory),
        IconRCategory(4, "Thể thao", IconR.listIconRCategory),
        IconRCategory(5, "Thể thao", IconR.listIconRCategory),
        IconRCategory(6, "Thể thao", IconR.listIconRCategory),
        IconRCategory(7, "Thể thao", IconR.listIconRCategory),
        IconRCategory(8, "Thể thao", IconR.listIconRCategory),
        IconRCategory(9, "Thể thao", IconR.listIconRCategory),
        IconRCategory(10, "Giải trí", IconR.listIconRCategory),
    )


}