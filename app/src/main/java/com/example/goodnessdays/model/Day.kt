package com.example.goodnessdays.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Day(
    val dayNum: Int,
    @StringRes val title: Int,
    @StringRes val subTitle: Int? = null,
    @DrawableRes val image: Int,
    @StringRes val description: Int
)