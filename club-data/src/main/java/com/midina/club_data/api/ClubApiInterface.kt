package com.midina.club_data.api

import com.midina.club_data.data.fixturesdata.FixturesData
import com.midina.club_data.data.teaminfodata.TeamInfoData
import com.midina.club_data.data.leaguepref.LeaguePreferences
import retrofit2.http.GET
import retrofit2.http.Query

private const val EPL_ID = 39

interface ClubApiInterface {
    @GET("/teams")
    suspend fun getTeamInfoData(
        @Query("id") id: Int
    ): TeamInfoData

    @GET("/leagues")
    suspend fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = EPL_ID
    ): LeaguePreferences

    @GET("/fixtures")
    suspend fun getFixtures(
        @Query("team") team: Int,
        @Query("season") season: Int,
        @Query("status") status: String = "ns"
    ): FixturesData
}
