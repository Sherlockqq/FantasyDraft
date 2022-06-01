package com.midina.match_data.data.weatherDescription


data class RetroWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDescription>,
    val message: Int
)