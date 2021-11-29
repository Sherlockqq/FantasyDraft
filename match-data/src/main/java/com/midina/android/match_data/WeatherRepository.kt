package com.midina.android.match_data

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.midina.android.match_data.api.WeatherApiInterface
import com.midina.android.match_data.data.WeatherDescription
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import java.io.EOFException
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val API_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApiInterface) {
    suspend fun getWeather(lat: Float, lon: Float, date: String): ResultEvent<MatchWeather> {
        return try {
            val retroWeather = weatherApi.getDataFromApi(lat, lon)
            val retroList = retroWeather.list
            Log.d("WeatherRepo", "${retroList.size}")
            val dateList = getDateList(retroList)
            val index = getIndex(date, dateList)
            Log.d("WeatherRepo", "CHECK")
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
        } catch (e: EOFException) {
            ResultEvent.Error
        }
    }

    private fun convertKelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273
    }

    private fun getDateList(list: List<WeatherDescription>): List<String> {
        val dateList = mutableListOf<String>()

        for (index in list.indices) {
            dateConverting(list[index].dtTxt).let { dateList.add(index, it) }
        }
        Log.d("WeatherRepo", "CHECK")
        return dateList
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateConverting(retroDate: String): String {

        val parser = SimpleDateFormat(API_DATE_PATTERN)
        val formatter = SimpleDateFormat(DATE_PATTERN)
        return formatter.format(parser.parse(retroDate))
    }

    private fun getIndex(matchDate: String, list: List<String>): Int? {

        for (index in list.indices) {
            if (getTimeInMillis(list[index]) - getTimeInMillis(matchDate) > 0) {
                Log.d("WeatherRepo", "SUCCESS")
                return index
            }
        }
        Log.d("WeatherRepo", "NULL")
        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTimeInMillis(matchTime: String): Long {

        val sdf = SimpleDateFormat(DATE_PATTERN)
        val matchDate = sdf.parse(matchTime)

        Log.d("FixturesViewModel", "Current Date: $matchDate")

        return matchDate.time
    }
}