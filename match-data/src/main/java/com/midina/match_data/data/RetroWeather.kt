package com.midina.match_data.data


data class RetroWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDescription>,
    val message: Int
)