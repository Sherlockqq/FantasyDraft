package com.midina.android.match_data.usecaseImpl

import com.midina.android.match_data.WeatherRepository
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import javax.inject.Inject

class GetWeatherUsecaseImpl @Inject constructor(val weatherRepository: WeatherRepository) :GetWeatherUsecase{
    override suspend fun execute(lat: Float,lon:Float,date:String): ResultEvent<MatchWeather> {
       return weatherRepository.getWeather(lat,lon,date)
    }
}

