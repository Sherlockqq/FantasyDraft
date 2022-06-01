package com.midina.matches_data.api

import com.midina.core_data.api.EPL_ID
import com.midina.matches_data.data.fixtures.FixturesData
import com.midina.matches_data.data.tour.CurrentTourData
import retrofit2.http.GET
import retrofit2.http.Query


interface FixturesApiInterface {
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