package com.cvd.qltaichinhcanhan.main.model.m_new

import android.content.Context
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_r.CategoryType

data class Category(
    var idCategory: String = "",
    var categoryName: String? = null,
    var type: Int? = null, // 1 ex , 2 in
    var moneyLimit: Float? = null,
    var selectCategory: Boolean? = null,
    var idIcon: String? = null,
    var idColor: Int? = null,
    var idUserAccount: String? = null,
)
fun getListCategoryCreateData(context: Context,idUserAccount:String): List<Category> {
    return arrayListOf(
        Category("", context.resources.getString(R.string.add), 1, 0F, false,"ic_add",8,idUserAccount),
        Category("", context.resources.getString(R.string.add), 2, 0F, false,"ic_add",8,idUserAccount),
        Category("", context.resources.getString(R.string.family), 1, 0F, false,"ic_k1",3,idUserAccount),
        Category("", context.resources.getString(R.string.move), 1, 0F, false,"ic_k2",2,idUserAccount),
        Category("", context.resources.getString(R.string.health), 1, 0F, false,"ic_k2",5,idUserAccount),
        Category("", context.resources.getString(R.string.other),1, 0F, false,"ic_k1",4,idUserAccount),
        Category("", context.resources.getString(R.string.wage), 2, 0F, false,"ic_k2",2,idUserAccount),
        Category("", context.resources.getString(R.string.bonus),2, 0F, false,"ic_k3",3,idUserAccount),
        Category("", context.resources.getString(R.string.invest), 2, 0F, false,"ic_k9",2,idUserAccount),
        Category("", context.resources.getString(R.string.other), 2, 0F, false,"ic_k5",1,idUserAccount),
    )
}

