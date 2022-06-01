package com.midina.match_data.api

import com.midina.match_data.data.matchData.MatchData
import com.midina.match_data.data.venueData.VenueData
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchApiInterface {
    @GET("/fixtures")
    suspend fun getMatchData(
        @Query("id") id: Int
    ): MatchData

    @GET("/teams")
    suspend fun getVenueData(
        @Query("id") id: Int
    ): VenueData
}