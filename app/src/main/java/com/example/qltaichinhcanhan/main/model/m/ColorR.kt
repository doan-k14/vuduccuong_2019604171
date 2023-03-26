package com.example.qltaichinhcanhan.main.model.m


data class ColorR(
    val id: Int = 0,
    var colorName: String? = null,
) {
    companion object {
        val listColorR = listOf<ColorR>(
            ColorR(1, "color1"),
            ColorR(2, "color2"),
            ColorR(3, "color3"),
            ColorR(4, "color4"),
            ColorR(5, "color5"),
            ColorR(6, "color6"),
            ColorR(7, "color7"),
            ColorR(8, "bg_color")
        )
    }
}