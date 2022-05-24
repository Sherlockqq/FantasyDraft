package com.midina.match_data.usecaseimpl

import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import com.midina.match_data.repositories.WeatherRepository
import javax.inject.Inject

class GetWeatherUsecaseImpl @Inject constructor(private val weatherRepository: WeatherRepository) :
    GetWeatherUsecase {
    override suspend fun execute(city: String, date: String): ResultEvent<MatchWeather> {
        return weatherRepository.getWeather(city, date)
    }
}

