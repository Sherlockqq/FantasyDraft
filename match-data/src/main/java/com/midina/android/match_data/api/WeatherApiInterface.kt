package com.midina.android.match_data.api

import com.midina.android.match_data.data.RetroWeather
import retrofit2.http.GET
import retrofit2.http.Query

//api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}

interface WeatherApiInterface {
    @GET("forecast")
    suspend fun getDataFromApi(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("appid") appKey: String = "c47829d28596bcea9b8793a6f45fb789"
    ): RetroWeather
}