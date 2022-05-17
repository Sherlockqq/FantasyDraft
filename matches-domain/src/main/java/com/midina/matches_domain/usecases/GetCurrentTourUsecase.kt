package com.midina.matches_domain.usecases

import com.midina.matches_domain.model.ResultEvent

interface GetCurrentTourUsecase {
    suspend fun execute(season: Int): ResultEvent<Int>
}