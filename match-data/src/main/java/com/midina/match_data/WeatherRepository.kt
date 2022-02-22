package com.midina.match_data

import android.annotation.SuppressLint
import android.util.Log
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.match_data.api.WeatherApiInterface
import com.midina.match_data.data.WeatherDescription
import java.io.EOFException
import java.text.SimpleDateFormat
import javax.inject.Inject

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val API_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
private const val TAG = "WeatherRepository"

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApiInterface) {
    suspend fun getWeather(lat: Float, lon: Float, date: String): ResultEvent<MatchWeather> {
        return try {
            val retroWeather = weatherApi.getCityWeather(lat, lon)
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
                        lat,
                        lon,
                        retroList[index].weather[0].main,
                        temperature,
                        date
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

    @SuppressLint("SimpleDateFormat")
    private fun dateConverting(retroDate: String): String {
        val parser = SimpleDateFormat(API_DATE_PATTERN)
        val formatter = SimpleDateFormat(DATE_PATTERN)
        return formatter.format(parser.parse(retroDate))
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