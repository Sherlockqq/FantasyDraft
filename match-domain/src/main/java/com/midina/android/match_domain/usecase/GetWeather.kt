package com.midina.android.match_domain.usecase

import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent

interface GetWeather {
    suspend fun execute(lat: Float,lon:Float,date:String): ResultEvent<MatchWeather>
}


