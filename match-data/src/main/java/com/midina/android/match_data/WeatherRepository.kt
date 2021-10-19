package com.midina.android.match_data

import android.util.Log
import com.midina.android.match_data.api.WeatherApiInterface
import com.midina.android.match_data.data.WeatherDescription
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import java.io.EOFException
import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApiInterface) {
    suspend fun getWeather(lat: Float, lon: Float, date: String): ResultEvent<MatchWeather> {
        return try {
            val retroWeather = weatherApi.getDataFromApi(lat, lon)
            val retroList = retroWeather.list
            Log.d("WeatherRepo", "${retroList.size}")
            val dateList = getDateList(retroList)
            val index = getIndex(date, dateList)
            val temperature =
                Math.round(convertKelvinToCelsius(retroList[index!!].main.temp) * 100.0) / 100.0
            Log.d("WeatherRepo", "CHECK")
            if (index == null) {
                ResultEvent.EmptyState
            } else {
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

    private fun getDateList(list: List<WeatherDescription>): List<LocalDateTime> {
        val dateList = mutableListOf<LocalDateTime>()

        for (index in list.indices) {
            dateList.add(index, dateConverting(list[index].dtTxt))
        }
        Log.d("WeatherRepo", "CHECK")
        return dateList
    }

    private fun dateConverting(retroDate: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val str = LocalDateTime.parse(retroDate, formatter).format(formatter2)
        return LocalDateTime.parse(str, formatter2)
    }

    private fun getIndex(matchDate: String, list: List<LocalDateTime>): Int? {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val date = LocalDateTime.parse(matchDate, formatter)
        for (index in list.indices) {
            if (date.isBefore(list[index])) {
                Log.d("WeatherRepo", "SUCCESS")
                return index
            }
        }
        Log.d("WeatherRepo", "NULL")
        return null
    }
}