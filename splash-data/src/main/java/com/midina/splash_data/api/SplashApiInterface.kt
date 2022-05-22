package com.midina.splash_data.api

import com.midina.splash_data.data.league.LeaguePreferences
import retrofit2.http.GET
import retrofit2.http.Query

const val EPL_ID = 39

interface SplashApiInterface {
    @GET("/leagues")
    suspend fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = EPL_ID
    ): LeaguePreferences
}