package com.midina.favourite_data.usecasesimpl

import com.midina.favourite_data.FavouriteRepository
import com.midina.favourite_domain.model.ResultEvent
import com.midina.favourite_domain.model.Team
import com.midina.favourite_domain.usecases.GetTeamsUsecase
import javax.inject.Inject

class GetTeamsUsecaseImpl @Inject constructor (
    private val matchRepository: FavouriteRepository
    ) : GetTeamsUsecase {
    override suspend fun execute(season: Int): ResultEvent<List<Team>> =
        matchRepository.getTeams(season)
}