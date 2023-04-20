package com.example.myweather.Model2


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String = ""
)