package com.midina.match_data.data.weatherDescription


import com.google.gson.annotations.SerializedName

data class WeatherDescription(
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val pop: Float,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)