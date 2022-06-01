package com.midina.favourite_domain.usecases

import com.midina.favourite_domain.model.ResultEvent
import com.midina.favourite_domain.model.Team

interface GetTeamsUsecase {
    suspend fun execute(season: Int): ResultEvent<List<Team>>
}