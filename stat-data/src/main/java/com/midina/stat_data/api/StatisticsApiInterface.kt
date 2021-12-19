package com.midina.stat_data.api

import com.midina.stat_data.data.LeaguePreferences
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface StatisticsApiInterface {
    @GET("leagues")
    fun getLeaguePreferences(
        @Query("current") current: Boolean = true,
        @Query("id") id: Int = 39,
        @Query("x-rapidapi-key") appKey: String = "40220fc8e6a6886380a261bcaea348c8"
    ): Single<LeaguePreferences>
}