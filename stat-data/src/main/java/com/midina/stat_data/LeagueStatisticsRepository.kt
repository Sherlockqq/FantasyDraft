package com.midina.stat_data

import android.util.Log
import com.midina.stat_data.api.StatisticsApiInterface
import com.midina.stat_data.topScorerData.TopScorers
import com.midina.stat_domain.model.ResultEvent
import com.midina.stat_domain.model.TopScorer
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "LeagueStatRepository"

class LeagueStatisticsRepository @Inject constructor(private val statApi: StatisticsApiInterface) {

    var season = 0

    fun getData() : Single<ResultEvent<Pair<Int, ResultEvent<TopScorer>>>> {
        return statApi.getLeaguePreferences()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "onError $it")
            }
            .flatMap{
                season = it.response[0].seasons[0].year
                return@flatMap getTopScorer(it.response[0].seasons[0].year)
            }
            .map {
                ResultEvent.Success(Pair(season,it))
            }
    }
    private fun getTopScorer(season: Int) : Single<ResultEvent<TopScorer>> {
          return statApi.getTopScorers(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
             .map {
                 ResultEvent.Success( convertPlayer(it))
             }
    }

    private fun convertPlayer(player: TopScorers) : TopScorer {
        val goals = player.response[0].statistics[0].goals.total
        val photoUrl = player.response[0].player.photo
        return TopScorer(goals, photoUrl)
    }
}