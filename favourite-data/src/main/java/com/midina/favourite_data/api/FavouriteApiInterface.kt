package com.midina.favourite_data.api

import com.midina.favourite_data.data.team.Teams
import retrofit2.http.GET
import retrofit2.http.Query

const val EPL_ID = 39

interface FavouriteApiInterface {
    @GET("/teams")
    suspend fun getTeams(
        @Query("season") season: Int,
        @Query("league") league: Int = EPL_ID
    ): Teams

}