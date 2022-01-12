package com.midina.stat_data.api

import com.midina.stat_data.seasonData.LeaguePreferences
import com.midina.stat_data.teamsData.TeamsData
import com.midina.stat_data.teamsStatistcsData.TeamsStatisticsData
import com.midina.stat_data.topPlayersData.TopPlayers
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
    ): Single<TopPlayers>

    @GET("/players/topassists")
    fun getTopAssistants(
        @Query("season") season: Int,
        @Query("league") league: Int = 39
    ): Single<TopPlayers>


    @GET("/teams")
    fun getTeamsId(
        @Query("season") season: Int,
        @Query("league") league: Int = 39
    ): Single<TeamsData>

    @GET("/teams/statistics")
    fun getTeamStatistic(
        @Query("season") season: Int,
        @Query("team") team: Int,
        @Query("league") league: Int = 39
    ): Single<TeamsStatisticsData>
}