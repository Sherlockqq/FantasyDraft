package com.midina.favourite_data

import com.midina.favourite_data.api.FavouriteApiInterface
import com.midina.favourite_data.data.team.Response
import com.midina.favourite_domain.model.ResultEvent
import com.midina.favourite_domain.model.Team
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteRepository @Inject constructor(private val favouriteApi: FavouriteApiInterface) {

    suspend fun getTeams(season: Int): ResultEvent<List<Team>> {
       return try {
            val result = favouriteApi.getTeams(season)
            val teams  = getListOfTeam(result.response)
            ResultEvent.Success(teams)
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }

    private fun getListOfTeam(response: List<Response>): List<Team> {
        val listOfTeam = mutableListOf<Team>()
        for (i in response.indices) {
            listOfTeam.add(response[i].toTeam())
        }
        return listOfTeam
    }

    private fun Response.toTeam(): Team {
        return Team(
            id = this.team.id,
            name = this.team.name,
            logo = this.team.logo
        )
    }

}