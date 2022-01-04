package com.midina.stat_data.api

import com.midina.stat_data.seasonData.LeaguePreferences
import com.midina.stat_data.topScorerData.TopScorers
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface StatisticsApiInterface {
    @GET("/leagues")
    fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = 39
    ): Single<LeaguePreferences>

    @GET("/players/topscorers")
    fun getTopScorers(
        @Query("season") season: Int,
        @Query("league") league: Int = 39
    ): Single<TopScorers>
}