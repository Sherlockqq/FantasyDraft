package com.midina.stat_data

import android.util.Log
import com.midina.stat_data.api.StatisticsApiInterface
import com.midina.stat_data.teamsData.TeamsData
import com.midina.stat_data.teamsStatistcsData.TeamsStatisticsData
import com.midina.stat_data.topPlayersData.TopPlayers
import com.midina.stat_domain.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

private const val TAG = "LeagueStatRepository"

class LeagueStatisticsRepository @Inject constructor(private val statApi: StatisticsApiInterface) {

    private var season = 0

    fun getAsyncData(): PublishSubject<ResultEvent<AsyncTopData>> {

        val subject: PublishSubject<ResultEvent<AsyncTopData>> = PublishSubject.create()

        getAsyncSeason().subscribe(object : SingleObserver<ResultEvent<AsyncTopData>> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "season onSub")
            }

            override fun onSuccess(result: ResultEvent<AsyncTopData>) {

                subject.onNext(result)

                getAsyncTopScorer().subscribe(object :
                    SingleObserver<ResultEvent<AsyncTopData>> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d(TAG, "topScorer onSub")

                    }

                    override fun onSuccess(result: ResultEvent<AsyncTopData>) {
                        subject.onNext(result)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "topScorer onErr")
                    }

                })

                getAsyncTopAssistant().subscribe(object :
                    SingleObserver<ResultEvent<AsyncTopData>> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d(TAG, "assistant onSub")
                    }

                    override fun onSuccess(result: ResultEvent<AsyncTopData>) {
                        subject.onNext(result)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "assistant onError $e")
                    }

                })

                getAsyncTopTeams().subscribe(object :
                    SingleObserver<ResultEvent<AsyncTopData>> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d(TAG, "topTeam onSub")
                    }

                    override fun onSuccess(result: ResultEvent<AsyncTopData>) {
                        subject.onNext(result)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "topTeam onError $e")
                    }

                })
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "season onErr")
            }

        })
        return subject
    }

    fun getData(): Single<ResultEvent<Pair<Int, TopData>>> {
        return statApi.getLeaguePreferences()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "onError $it")
            }
            .flatMap {
                season = it.response[0].seasons[0].year

                Single.zip(
                    getTopScorer(),
                    getTopAssistant(),
                    getTopTeams(),
                    { scorer, assistant, teamStat ->
                        TopData(scorer, assistant, teamStat)
                    })
            }
            .map {
                ResultEvent.Success(Pair(season, it))
            }
    }

    private fun getAsyncSeason(): Single<ResultEvent<AsyncTopData>> {
        return statApi.getLeaguePreferences()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "onError $it")
            }
            .map {
                season = it.response[0].seasons[0].year
                //AsyncTopData.AsyncSeason(season)
                ResultEvent.Success(AsyncTopData.AsyncSeason(it.response[0].seasons[0].year))
            }
    }

    private fun getTopScorer(): Single<ResultEvent<TopScorer>> {
        return statApi.getTopScorers(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .map {
                ResultEvent.Success(it.toTopScorer())
            }
    }

    private fun getAsyncTopScorer(): Single<ResultEvent<AsyncTopData>> {
        return statApi.getTopScorers(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .map {
                ResultEvent.Success(AsyncTopData.AsyncTopScorer(it.toTopScorer()))
            }
    }

    private fun getTopAssistant(): Single<ResultEvent<TopAssistant>> {
        return statApi.getTopAssistants(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .map {
               ResultEvent.Success(it.toTopAssistant())
            }
    }

    private fun getAsyncTopAssistant(): Single<ResultEvent<AsyncTopData>> {
        return statApi.getTopAssistants(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .map {
                ResultEvent.Success(AsyncTopData.AsyncTopAssistant(it.toTopAssistant()))
            }
    }

    private fun getTopTeams(): Single<ResultEvent<Pair<TopCleanSheet?, TopTeamGoals?>>> {
        return statApi.getTeamsId(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .flatMap {
                getTeamsStatistics(it.toListOfId())
            }
            .map {
                Pair(it.toListOfTopCleanSheet(), it.toListOfTopTeamGoals())
            }
            .map { list ->
                Pair(list.first.maxByOrNull { it.cleanSheets },
                    list.second.maxByOrNull { it.goals })
            }
            .map {
                ResultEvent.Success(it)
            }
    }

    private fun getAsyncTopTeams(): Single<ResultEvent<AsyncTopData>> {
        return statApi.getTeamsId(season)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d(TAG, "getTop onSuccess $it")
            }
            .doOnError {
                Log.d(TAG, "getTop onError $it")
            }
            .flatMap {
                getTeamsStatistics(it.toListOfId())
            }
            .map {
                Pair(it.toListOfTopCleanSheet(), it.toListOfTopTeamGoals())
            }
            .map { list ->
                Pair(list.first.maxByOrNull { it.cleanSheets },
                    list.second.maxByOrNull { it.goals })
            }
            .map {
                ResultEvent.Success(AsyncTopData.AsyncTopTeams(it))
            }
    }

    private fun getTeamsStatistics(team: List<Int>): Single<List<TeamsStatisticsData>> {
        return Observable.fromIterable(team)
            .flatMapSingle { statApi.getTeamStatistic(season, it) }
            .toList()
    }

    private fun TopPlayers.toTopScorer(): TopScorer {
        val goals = this.response[0].statistics[0].goals.total
        val photoUrl = this.response[0].player.photo
        return TopScorer(goals, photoUrl)
    }

    private fun TopPlayers.toTopAssistant(): TopAssistant {
        val assists = this.response[0].statistics[0].goals.assists
        val photoUrl = this.response[0].player.photo
        return TopAssistant(assists, photoUrl)
    }

    private fun TeamsData.toListOfId(): List<Int> {
        val listOfId: MutableList<Int> = mutableListOf()
        val response = this.response
        for (item in response) {
            listOfId.add(item.team.id)
        }
        // listOfId.add(this.response[0].team.id)
        return listOfId
    }

    private fun TeamsStatisticsData.toTopCleanSheet(): TopCleanSheet {
        val cleanSheet = this.response.clean_sheet.total
        val photoUrl = this.response.team.logo
        return TopCleanSheet(cleanSheet, photoUrl)
    }

    private fun TeamsStatisticsData.toTopTeamGoals(): TopTeamGoals {
        val goals = this.response.goals.`for`.total.total
        val photoUrl = this.response.team.logo
        return TopTeamGoals(goals, photoUrl)
    }

    private fun List<TeamsStatisticsData>.toListOfTopCleanSheet(): List<TopCleanSheet> {
        val listOfTopCleanSheet: MutableList<TopCleanSheet> = mutableListOf()
        for (item in this) {
            listOfTopCleanSheet.add(item.toTopCleanSheet())
        }
        return listOfTopCleanSheet
    }

    private fun List<TeamsStatisticsData>.toListOfTopTeamGoals(): List<TopTeamGoals> {
        val listOfTopTeamGoals: MutableList<TopTeamGoals> = mutableListOf()
        for (item in this) {
            listOfTopTeamGoals.add(item.toTopTeamGoals())
        }
        return listOfTopTeamGoals
    }

}