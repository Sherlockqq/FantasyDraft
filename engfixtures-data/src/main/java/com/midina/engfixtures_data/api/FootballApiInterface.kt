package com.midina.engfixtures_data.api

import com.midina.engfixtures_data.data.FixturesData
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballApiInterface {
    @GET("/fixtures")
    suspend fun getFixtures(
        @Query("season") season: Int = 2021,
        @Query("league") league: Int = 39
    ): FixturesData
}