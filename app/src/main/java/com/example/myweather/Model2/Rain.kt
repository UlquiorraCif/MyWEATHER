package com.example.myweather.Model2


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double = 0.0
)