package com.example.myweather.Activities



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.Model2.ForecastItem
import com.example.myweather.databinding.ForecastItemBinding

class ForecastAdapter(private val forecastItems: List<ForecastItem>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(private val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: ForecastItem) {
            binding.apply {
                forecastTimeTextView.text = forecast.dtTxt
                forecastTempTextView.text = forecast.main.temp.toString()
                forecastDescriptionTextView.text =
                    forecast.weather.firstOrNull()?.description.orEmpty()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding =
            ForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecastItem = forecastItems[position]
        holder.bind(forecastItem)
    }

    override fun getItemCount() = forecastItems.size

}
