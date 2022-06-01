package com.midina.android.match_domain.usecase

import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent

interface GetWeatherUsecase {
    suspend fun execute(city: String,date:String): ResultEvent<MatchWeather>
}


