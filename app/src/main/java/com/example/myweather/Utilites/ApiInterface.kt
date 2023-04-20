package com.example.myweather.Utilites





import com.example.myweather.Model2.FiveDayForecastItem

import com.example.myweather.Models.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {



    @GET("weather")
    fun getCityWeatherData(
        @Query("q") q:String,
        @Query("APPID") appid:String
    ): Call<WeatherModel>
    @GET("forecast")
    fun getFiveDayForecastData(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<FiveDayForecastItem>





}