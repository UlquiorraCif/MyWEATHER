package com.example.myweather.Model2


import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: Int = 0,
    @SerializedName("lon")
    val lon: Double = 0.0
)