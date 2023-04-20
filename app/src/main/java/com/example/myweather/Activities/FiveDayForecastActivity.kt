package com.example.myweather.Activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.Model2.FiveDayForecastItem
import com.example.myweather.Utilites.ApiUtilities
import com.example.myweather.databinding.ForecastItemBinding


class FiveDayForecastActivity : AppCompatActivity() {
    private lateinit var binding: ForecastItemBinding
    private var city: String? = null
    private val apiKey = "2dd3806c050f7e67309bb1de02539e23"
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForecastItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.city = null
        this.city = intent.getStringExtra("city")

        if (city != null) {
            getFiveDayForecast(city!!)
        } else {
            Toast.makeText(
                this@FiveDayForecastActivity,
                "Не удалось получить данные прогноза.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun getFiveDayForecast(city: String) {
        val apiInterface = ApiUtilities.getApiInterface()
        val call = apiInterface?.getFiveDayForecastData(city, apiKey)

        call?.enqueue(object : Callback<FiveDayForecastItem> {
            override fun onResponse(
                call: Call<FiveDayForecastItem>,
                response: Response<FiveDayForecastItem>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { forecastModel ->
                        setData(forecastModel)
                    }
                } else {
                    Toast.makeText(
                        this@FiveDayForecastActivity,
                        "Не удалось получить данные прогноза.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<FiveDayForecastItem>, t: Throwable) {
                Toast.makeText(
                    this@FiveDayForecastActivity,
                    "Не удалось получить данные прогноза.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setData(forecastModel: FiveDayForecastItem) {
        val forecastRecyclerView: RecyclerView = binding.forecastRecyclerView
        forecastAdapter = ForecastAdapter(forecastModel.list)
        forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        forecastRecyclerView.adapter = forecastAdapter
    }

}