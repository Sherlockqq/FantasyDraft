package com.midina.stat_data

import android.util.Log
import com.midina.stat_data.api.StatisticsApiInterface
import com.midina.stat_data.data.LeaguePreferences
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "LeagueStatRepository"

class LeagueStatisticsRepository @Inject constructor(private val statApi: StatisticsApiInterface) {
    fun getSeason() : Single<LeaguePreferences> {
        return statApi.getLeaguePreferences()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "onError $it")
            }
    }
}