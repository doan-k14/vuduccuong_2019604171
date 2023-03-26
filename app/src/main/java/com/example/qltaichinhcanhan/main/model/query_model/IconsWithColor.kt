package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m.ColorR
import com.example.qltaichinhcanhan.main.model.m.IconR

data class IconsWithColor(
    @Embedded val icon: IconR,
    @Relation(
        parentColumn = "idColorR",
        entityColumn = "id"
    )
    val color: ColorR?
)