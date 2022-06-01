package com.midina.match_data.repositories

import android.annotation.SuppressLint
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.match_data.api.WeatherApiInterface
import com.midina.match_data.data.weatherDescription.WeatherDescription
import java.text.SimpleDateFormat
import javax.inject.Inject

private const val DATE_PATTERN = "yyyy-MM-DD HH:mm"
private const val TAG = "WeatherRepository"

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApiInterface) {
    suspend fun getWeather(city: String, date: String): ResultEvent<MatchWeather> {
        return try {
            val retroWeather = weatherApi.getCityWeather(city)
            val retroList = retroWeather.list
            val dateMap = getDateMap(retroList, date)
            val index = getIndex(date, dateMap)
            if (index == null) {
                ResultEvent.EmptyState
            } else {
                val temperature =
                    Math.round(convertKelvinToCelsius(retroList[index].main.temp) * 100.0) / 100.0
                ResultEvent.Success(
                    MatchWeather(
                        retroList[index].weather[0].main,
                        temperature
                    )
                )
            }
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }

    private fun convertKelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273
    }

    private fun getDateMap(list: List<WeatherDescription>, matchDate: String): Map<Int, String> {
        val dateMap = mutableMapOf<Int, String>()

        for (index in list.indices) {
            dateConverting(list[index].dtTxt).let {
                if (checkDate(it, matchDate)) {
                    dateMap[index] = it
                }
            }
        }
        return dateMap
    }

    private fun checkDate(weatherDate: String, matchDate: String): Boolean {
        return weatherDate.dropLast(5) == matchDate.dropLast(5)
    }

    private fun dateConverting(retroDate: String): String {
        val r = retroDate.take(16)
        return r
    }

    private fun getIndex(matchDate: String, map: Map<Int, String>): Int? {
        for (item in map) {

            if (getTimeInMillis(item.value) - getTimeInMillis(matchDate) >= 0) {
                return item.key
            }
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTimeInMillis(matchTime: String): Long {
        val sdf = SimpleDateFormat(DATE_PATTERN)
        val matchDate = sdf.parse(matchTime)

        return matchDate.time
    }
}