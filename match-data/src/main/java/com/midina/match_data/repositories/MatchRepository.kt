package com.midina.match_data.repositories

import android.util.Log
import com.midina.android.match_domain.model.*
import com.midina.match_data.api.MatchApiInterface
import com.midina.match_data.data.matchData.MatchData
import com.midina.match_data.data.venueData.VenueData
import java.lang.StringBuilder
import javax.inject.Inject

private const val TAG = "MatchRepository"

class MatchRepository @Inject constructor(private val matchApiInterface: MatchApiInterface) {
    suspend fun getMatch(matchId: Int): ResultEvent<Match> {
        return try {
            val matchData = matchApiInterface.getMatchData(matchId)
            val venueData = matchApiInterface.getVenueData(matchData.response[0].teams.home.id)
            ResultEvent.Success(matchData.toMatch(venueData))
        } catch (e: Exception) {
            Log.d(TAG,"Error: $e")
            ResultEvent.Error
        }
    }

    private fun MatchData.toMatch(venueData: VenueData): Match {
        return Match(
            toFixture(venueData),
            toTeams(),
            toGoals()
        )
    }

    private fun MatchData.toFixture(venueData: VenueData): Fixture {
        return Fixture(
            getDateByPattern(response[0].fixture.date),
            venueData.toVenue()
        )
    }

    private fun VenueData.toVenue(): Venue {
        return Venue(
            response[0].venue.city,
            response[0].venue.image
        )
    }

    private fun MatchData.toGoals(): Goals {
        return Goals(
            response[0].goals.away,
            response[0].goals.home
        )
    }

    private fun MatchData.toTeams(): Teams {
        return Teams(
            toAway(),
            toHome()
        )
    }

    private fun MatchData.toAway(): Away {
        return Away(
            response[0].teams.away.logo,
            response[0].teams.away.name
        )
    }

    private fun MatchData.toHome(): Home {
        return Home(
            response[0].teams.home.logo,
            response[0].teams.home.name
        )
    }

    private fun getDateByPattern(date: String): String {
        val str = StringBuilder(date.substring(0, date.length - 9))
        str[10] = ' '
        return str.toString()
    }
}