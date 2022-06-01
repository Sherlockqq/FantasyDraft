package com.midina.matches_data.usecaseimpl

import com.midina.matches_data.FixturesRepository
import com.midina.matches_data.data.fixtures.FixturesData
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import java.lang.StringBuilder
import javax.inject.Inject

class GetMatchesScheduleUsecaseImpl @Inject constructor
    (private val fixturesRepository: FixturesRepository) : GetMatchesScheduleUsecase {
    override suspend fun execute(season: Int): ResultEvent<Map<Int, ArrayList<MatchSchedule>>> {
        return try {
            val fixturesData = fixturesRepository.getMatchMap(season)

            if (fixturesData.results == 0) {
                ResultEvent.EmptyState
            } else {
                ResultEvent.Success(fixturesData.toMapOfMatchSchedule())
            }

        } catch (e: Exception) {
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

    private fun getDateByPattern(date: String): String {
        val str = StringBuilder(date.substring(0, date.length - 9))
        str[10] = ' '
        return str.toString()
    }
}