package com.example.myweather.Model2


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int = 0
)