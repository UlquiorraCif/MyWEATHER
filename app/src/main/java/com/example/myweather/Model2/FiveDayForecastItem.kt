package com.example.myweather.Model2



import com.google.gson.annotations.SerializedName

data class FiveDayForecastItem(
    @SerializedName("city")
    val city: City = City(),
    @SerializedName("cnt")
    val cnt: Int = 0,
    @SerializedName("cod")
    val cod: String = "",
    @SerializedName("list")
    val list: List<ForecastItem> = listOf(),
    @SerializedName("message")
    val message: Int = 0
)