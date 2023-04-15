package com.example.qltaichinhcanhan.main.model.m

import android.content.Context
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.adapter.AdapterLanguage

class LanguageR(
    var idLanguageR: Int = 0,
    var nameLanguageR: Int? = null,
    var nameLanguageR1: Int? = null,
    var flagLanguage: Int? = null,
    var codeLanguage: String? = null,
    var selectLanguage: Boolean? = null,
) {
    companion object {
        val listLanguage = listOf<LanguageR>(
            LanguageR(1, R.string.vn, R.string.tieng_viet, R.drawable.ic_vn, "vi", false),
            LanguageR(2, R.string.gb, R.string.tieng_viet, R.drawable.ic_gb, "en", false),
            LanguageR(3, R.string.cn, R.string.tieng_viet, R.drawable.ic_cn, "zh", false),
            LanguageR(4, R.string.in_l, R.string.tieng_viet, R.drawable.ic_in, "in", false),
            LanguageR(5, R.string.la, R.string.tieng_viet, R.drawable.ic_la, "lo", false),
            LanguageR(6, R.string.kr, R.string.tieng_viet, R.drawable.ic_kr, "ko", false),
            LanguageR(7, R.string.de, R.string.tieng_viet, R.drawable.ic_de, "de", false),
            LanguageR(8, R.string.jp, R.string.tieng_viet, R.drawable.ic_jp, "ja", false),
            LanguageR(9, R.string.fr, R.string.tieng_viet, R.drawable.ic_fr, "fr", false),
        )
    }
}