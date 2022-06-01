package com.midina.matches_data

import android.util.Log
import com.midina.matches_data.api.FixturesApiInterface
import com.midina.matches_data.data.fixtures.FixturesData
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

private const val DATE_PATTERN = "yyyy-MM.DD HH:mm"

@Singleton
class MatchRepository @Inject constructor(private val fixturesApi: FixturesApiInterface) {

    suspend fun getMatchMap(): ResultEvent<Map<Int, ArrayList<MatchSchedule>>> {
        return try {
            val league = fixturesApi.getLeaguePreferences()
            val season = league.response[0].seasons[0].year
            val fixturesData = fixturesApi.getFixtures(season = season)

            ResultEvent.Success(
                fixturesData.toMapOfMatchSchedule()
            )
        } catch (e: Exception) {
            Log.d("MatchRepo", "$e")
            ResultEvent.Error
        }
    }

    suspend fun getTour(season: Int): ResultEvent<Int> {
        return try {
            val tour =
                fixturesApi
                    .getCurrentTour(season = season)
                    .response[0]
                    .takeLast(2)
                    .trim()
                    .toInt()
            ResultEvent.Success(tour)
        } catch (e: Exception) {
            Log.d("MatchRepo","$e")
            ResultEvent.Error
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun FixturesData.toMapOfMatchSchedule(): Map<Int, ArrayList<MatchSchedule>> {
        val arrayOfMatchSchedule = arrayListOf<MatchSchedule>()
        val mapOfMatches = mutableMapOf<Int, ArrayList<MatchSchedule>>()
        var countTour = 1
        for (i in this.response.indices) {
            val tour = this.response[i].league.round.takeLast(2).trim().toInt()

            if (countTour != tour) {
                mapOfMatches[countTour] = arrayOfMatchSchedule.clone() as ArrayList<MatchSchedule>
                arrayOfMatchSchedule.clear()
                countTour++
            }
            val homeName = this.response[i].teams.home.name
            val homeLogo = this.response[i].teams.home.logo
            val awayName = this.response[i].teams.away.name
            val awayLogo = this.response[i].teams.away.logo
            val date = getDateByPattern(this.response[i].fixture.date)
            val score =
                this.response[i].score.fulltime.home.toString() + " : " +
                        this.response[i].score.fulltime.away.toString()
            val match = MatchSchedule(
                tour,
                homeName,
                homeLogo,
                awayName,
                awayLogo,
                date,
                score
            )
            arrayOfMatchSchedule.add(match)
        }
        mapOfMatches[countTour] = arrayOfMatchSchedule
        return mapOfMatches
    }
//2021-08-13T19:00:00+00:00

    private fun getDateByPattern(date: String): String {
        val str = StringBuilder(date.substring(0, date.length - 9))
        str[10] = ' '
        return str.toString()
    }
}