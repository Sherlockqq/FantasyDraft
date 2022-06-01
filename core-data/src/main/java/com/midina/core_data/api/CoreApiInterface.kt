package com.midina.core_data.api

import com.midina.core_data.data.leaguepref.LeaguePreferences
import retrofit2.http.GET
import retrofit2.http.Query

const val EPL_ID = 39

interface CoreApiInterface {
    @GET("/leagues")
    suspend fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = EPL_ID
    ): LeaguePreferences
}
