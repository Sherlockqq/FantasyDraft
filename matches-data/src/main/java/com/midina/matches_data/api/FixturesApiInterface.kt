package com.midina.matches_data.api

import com.midina.matches_data.data.fixtures.FixturesData
import com.midina.matches_data.data.league.LeaguePreferences
import com.midina.matches_data.data.tour.CurrentTourData
import retrofit2.http.GET
import retrofit2.http.Query

const val EPL_ID = 39

interface FixturesApiInterface {
    @GET("/leagues")
    suspend fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = EPL_ID
    ): LeaguePreferences

    @GET("/fixtures")
    suspend fun getFixtures(
        @Query("league") league: Int = EPL_ID,
        @Query("season") season: Int
    ) : FixturesData

    @GET("/fixtures/rounds")
    suspend fun getCurrentTour(
        @Query("league") league: Int = EPL_ID,
        @Query("season") season: Int,
        @Query("current") current: Boolean = true
    ): CurrentTourData
}