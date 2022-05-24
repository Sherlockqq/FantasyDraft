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

@Singleton
class FixturesRepository @Inject constructor(private val fixturesApi: FixturesApiInterface) {

    //TODO MAKE GET SEASON IN CORE DATA

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
            val currentTourData = fixturesApi.getCurrentTour(season = season)

            if (currentTourData.response.isEmpty()) {
                ResultEvent.Success(0)
            } else {
                ResultEvent.Success(
                    currentTourData.response[0]
                        .takeLast(2)
                        .trim()
                        .toInt()
                )
            }
        } catch (e: Exception) {
            Log.d("MatchRepo", "$e")
            ResultEvent.Error
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun FixturesData.toMapOfMatchSchedule(): Map<Int, ArrayList<MatchSchedule>> {
        val arrayOfMatchSchedule = arrayListOf<MatchSchedule>()
        val mapOfMatches = mutableMapOf<Int, ArrayList<MatchSchedule>>()
        var countTour = 1
        for (i in this.response.indices) {
            val fixtureId = this.response[i].fixture.id
            val tour = this.response[i].league.round.takeLast(2).trim().toInt()

            if (countTour != tour) {
                mapOfMatches[countTour] = arrayOfMatchSchedule.clone() as ArrayList<MatchSchedule>
                arrayOfMatchSchedule.clear()
                countTour++
            }
            val homeName = response[i].teams.home.name
            val homeLogo = response[i].teams.home.logo
            val awayName = response[i].teams.away.name
            val awayLogo = response[i].teams.away.logo
            val date = getDateByPattern(response[i].fixture.date)
            val status = response[i].fixture.status.short
            var score = ""
            score = if (status == "NS") {
                "? : ?"
            } else {
                response[i].score.fulltime.home.toString() + " : " +
                        response[i].score.fulltime.away.toString()
            }

            val match = MatchSchedule(
                fixtureId,
                tour,
                homeName,
                homeLogo,
                awayName,
                awayLogo,
                date,
                score,
                status
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
