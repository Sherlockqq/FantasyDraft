package com.midina.engfixtures_data

import android.util.Log
import com.midina.engfixtures_data.api.FootballApiInterface
import com.midina.engfixtures_data.data.Response
import com.midina.engfixtures_domain.model.Match
import com.midina.engfixtures_domain.model.ResultEvent
import javax.inject.Inject

private const val TAG = "FixturesRepository"

class FixturesRepository @Inject constructor(private val footballApi: FootballApiInterface) {
    suspend fun getFixtures() : ResultEvent<Map<Int, ArrayList<Match>>> {
        return try {
            val response = footballApi.getFixtures().response
            val matchArray = arrayListOf<Match>()
            for (item in response) {
                matchArray.add(item.toMatch())
            }
            return ResultEvent.Success(matchArray.toMap())
        }
        catch (e: Exception) {
            Log.d(TAG, "Exc $e")
            ResultEvent.Error
        }
    }

    private fun Response.toMatch(): Match {
        return Match(
            getMatchId(this),
            getMatchHomeTeamId(this),
            getMatchHomeTeamName(this),
            getMatchHomeTeamLogo(this),
            getMatchHomeTeamGoals(this),
            getMatchAwayTeamId(this),
            getMatchAwayTeamName(this),
            getMatchAwayTeamLogo(this),
            getMatchAwayTeamGoals(this),
            getMatchStatus(this),
            getMatchDate(this),
            getMatchTour(this)
        )
    }

    private fun getMatchId(item: Response): Int {
        return item.fixture.id
    }

    private fun getMatchHomeTeamId(item: Response): Int {
        return item.teams.home.id
    }

    private fun getMatchHomeTeamName(item: Response): String {
        return item.teams.home.name
    }

    private fun getMatchHomeTeamLogo(item: Response): String {
        return item.teams.home.logo
    }

    private fun getMatchHomeTeamGoals(item: Response): Int {
        return item.goals.home
    }

    private fun getMatchAwayTeamId(item: Response): Int {
        return item.teams.away.id
    }

    private fun getMatchAwayTeamName(item: Response): String {
        return item.teams.away.name
    }

    private fun getMatchAwayTeamLogo(item: Response): String {
        return item.teams.away.logo
    }

    private fun getMatchAwayTeamGoals(item: Response): Int {
        return item.goals.away
    }

    private fun getMatchStatus(item: Response): String {
        return item.fixture.status.short
    }

    private fun getMatchDate(item: Response): String {
        return item.fixture.date
    }

    private fun getMatchTour(item: Response): Int {
        return item.league.round
            .replace("Regular Season - ", "")
            .toInt()
    }

    private fun ArrayList<Match>.toMap(): Map<Int, ArrayList<Match>> {
        val matchesMap: MutableMap<Int, ArrayList<Match>> = mutableMapOf()
        var tour = 1
        var arraylistOfTourMatches = arrayListOf<Match>()
        //var listOfTourMatches = mutableListOf<Match>()
        for(item in this) {
            val temp = item.tour
            Log.d(TAG, "tour $temp")
            if(item.tour == tour) {
                arraylistOfTourMatches.add(item)
            } else {
                matchesMap[tour] = arraylistOfTourMatches
                tour = item.tour
                arraylistOfTourMatches = arrayListOf()
                arraylistOfTourMatches.add(item)
                Log.d(TAG, "for $matchesMap")
            }
        }
        Log.d(TAG, "result $matchesMap")
        return matchesMap
    }
}