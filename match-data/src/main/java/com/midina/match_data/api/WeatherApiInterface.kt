package com.midina.match_data.api

import com.midina.match_data.data.weatherDescription.RetroWeather
import retrofit2.http.GET
import retrofit2.http.Query

//api.openweathermap.org/data/2.5/forecast?q=Londong&appid=c47829d28596bcea9b8793a6f45fb789

interface WeatherApiInterface {
    @GET("forecast")
    suspend fun getCityWeather(
        @Query("q") city: String,
        @Query("appid") appKey: String = "c47829d28596bcea9b8793a6f45fb789"
    ): RetroWeather
}