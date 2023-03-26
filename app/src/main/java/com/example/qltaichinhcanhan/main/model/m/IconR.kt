package com.example.qltaichinhcanhan.main.model.m

data class IconR(
    val id: Int = 0,
    var iconName: String? = null,
    var type: Int? = null,
    var select: Boolean? = null,
    var idColorR: Int? = null,
) {
    companion object {
        val listIconR = listOf<IconR>(
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
            IconR(19, "ic_account21", 1, false, 1),
            IconR(2, "ic_ms1", 0, false, 1),
            IconR(3, "ic_ms2", 0, false, 1),
            IconR(4, "ic_ms3", 0, false, 1),
            IconR(5, "ic_ms4", 0, false, 1),
            IconR(6, "ic_ms5", 0, false, 1),
            IconR(7, "ic_ms5", 0, false, 1),
            IconR(8, "ic_ms5", 0, false, 1),
            IconR(9, "ic_ms5", 0, false, 1),
            IconR(10, "ic_ms5", 0, false, 1),
            IconR(11, "ic_ms5", 0, false, 1),
            IconR(12, "ic_sk", 0, false, 1),
        )
    }

}