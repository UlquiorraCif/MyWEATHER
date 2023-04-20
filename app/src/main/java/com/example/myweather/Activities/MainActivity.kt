package com.example.myweather.Activities
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myweather.Models.WeatherModel
import com.example.myweather.R
import com.example.myweather.Utilites.ApiUtilities
import com.example.myweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

open class MainActivity : AppCompatActivity() {

    // Поле для привязки макета ActivityMainBinding
    lateinit var binding: ActivityMainBinding

    // Ключ API для OpenWeatherMap
    private val apiKey = "2dd3806c050f7e67309bb1de02539e23"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Устанавливаем макет, связанный с данной Activity
        setContentView(R.layout.activity_main)

        // Связываем привязку макета с данными
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.buttonShow5dayForecast.visibility= View.GONE

        // Устанавливаем слушатель для кнопки прогноза погоды на 5 дней
        binding.buttonShow5dayForecast.setOnClickListener {
            val intent = Intent(this, FiveDayForecastActivity::class.java)

            intent.putExtra("city", binding.citySearch.text.toString())


            startActivity(intent)


        }




        // Устанавливаем слушатель действия редактирования поля ввода города
        binding.citySearch.setOnEditorActionListener { textView, i, keyEvent ->
            // Если нажата кнопка поиска
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                // Вызываем метод для получения погоды для указанного города
                getCityWeather(binding.citySearch.text.toString())
                // Получаем фокус текущего представления и скрываем клавиатуру
                val view = this.currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.citySearch.clearFocus()
                }
                //  действие было обработано
                return@setOnEditorActionListener true
            } else {
                //  действие не было обработано
                return@setOnEditorActionListener false
            }
        }
    }

    // Метод для получения погоды для указанного города
    private fun getCityWeather(city: String) {


        // Вызываем API-интерфейс для получения данных о погоде
        ApiUtilities.getApiInterface()?.getCityWeatherData(city.trim(), apiKey)
            ?.enqueue(object : Callback<WeatherModel> {

                // Если запрос успешен
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { weatherModel ->
                            setData(weatherModel)
                            binding.buttonShow5dayForecast.visibility= View.VISIBLE
                        }

                    } else {

                        Toast.makeText(
                            this@MainActivity, "No City Found",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    // Обрабатываем ошибки


                }


                @SuppressLint("SetTextI18n", "SimpleDateFormat")
                @RequiresApi(Build.VERSION_CODES.O)
                private fun setData(body: WeatherModel) {

                    binding.apply {

                        val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())

                        dateTime.text = currentDate.toString()

                        maxTemp.text = "Max " + k2c(body.main.temp_max) + "°"

                        minTemp.text = "Min " + k2c(body.main.temp_min) + "°"

                        temp.text = "" + k2c(body.main.temp) + "°"

                        weatherTitle.text = body.weather[0].main

                        sunriseValue.text = ts2td(body.sys.sunrise.toLong())

                        sunsetValue.text = ts2td(body.sys.sunset.toLong())

                        pressureValue.text = body.main.pressure.toString()

                        humidityValue.text = body.main.humidity.toString() + "%"

                        tempFValue.text = "" + (k2c(body.main.temp).times(1.8)).plus(32)
                            .roundToInt() + "°"

                        citySearch.setText(body.name)

                        feelsLike.text = "" + k2c(body.main.feels_like) + "°"

                        windValue.text = body.wind.speed.toString() + "m/s"

                        groundValue.text = body.main.grnd_level.toString()

                        seaValue.text = body.main.sea_level.toString()

                        countryValue.text = body.sys.country


                    }

                    updateUI(body.weather[0].id)


                }

                @RequiresApi(Build.VERSION_CODES.O)
                private fun ts2td(ts: Long): String {

                    val localTime = ts.let {

                        Instant.ofEpochSecond(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()

                    }

                    return localTime.toString()


                }

                private fun k2c(t: Double): Double {

                    var intTemp = t

                    intTemp = intTemp.minus(273)

                    return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                }


                private fun updateUI(id: Int) {

                    binding.apply {


                        when (id) {

                            //Гроза
                            in 200..232 -> {

                                weatherImg.setImageResource(R.drawable.ic_storm_weather)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.thunderstrom_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.thunderstrom_bg)


                            }

                            //облака
                            in 300..321 -> {

                                weatherImg.setImageResource(R.drawable.ic_few_clouds)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.drizzle_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.drizzle_bg)


                            }

                            //дождь
                            in 500..531 -> {

                                weatherImg.setImageResource(R.drawable.ic_rainy_weather)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.rain_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.rain_bg)

                            }

                            //снег
                            in 600..622 -> {

                                weatherImg.setImageResource(R.drawable.ic_snow_weather)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.snow_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.snow_bg)

                            }

                            //тумвн
                            in 701..781 -> {

                                weatherImg.setImageResource(R.drawable.ic_broken_clouds)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.atmosphere_bg)


                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.atmosphere_bg)

                            }

                            //солнечно
                            800 -> {

                                weatherImg.setImageResource(R.drawable.ic_clear_day)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.clear_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.clear_bg)

                            }

                            //облачно
                            in 801..804 -> {

                                weatherImg.setImageResource(R.drawable.ic_cloudy_weather)

                                mainLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.clouds_bg)

                                optionsLayout.background = ContextCompat
                                    .getDrawable(this@MainActivity, R.drawable.clouds_bg)

                            }




                        }


                    }


                }


            })
    }
}


