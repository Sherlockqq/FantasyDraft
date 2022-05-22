package com.midina.splash_data

import com.midina.splash_data.api.SplashApiInterface
import com.midina.splash_domain.model.ResultEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashRepository @Inject constructor(private val splashApiInterface: SplashApiInterface) {
    suspend fun getCurrentSeason(): ResultEvent<Int> {
        return try {
            val league = splashApiInterface.getLeaguePreferences()
            val season = league.response[0].seasons[0].year
            ResultEvent.Success(season)
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }
}