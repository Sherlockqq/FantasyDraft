package com.midina.club_data.api

import com.midina.club_data.data.fixturesdata.FixturesData
import com.midina.club_data.data.playersdata.PlayersData
import com.midina.club_data.data.teaminfodata.TeamInfoData
import retrofit2.http.GET
import retrofit2.http.Query

interface ClubApiInterface {
    @GET("/teams")
    suspend fun getTeamInfoData(
        @Query("id") id: Int
    ): TeamInfoData

    @GET("/fixtures")
    suspend fun getFixtures(
        @Query("team") team: Int,
        @Query("season") season: Int,
        @Query("status") status: String = "ns"
    ): FixturesData

    @GET("/players")
    suspend fun getPlayers(
        @Query("team") team: Int,
        @Query("season") season: Int,
    ): PlayersData
}
