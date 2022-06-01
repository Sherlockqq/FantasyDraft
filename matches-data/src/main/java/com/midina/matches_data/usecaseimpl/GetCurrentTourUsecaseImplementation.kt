package com.midina.matches_data.usecaseimpl

import com.midina.matches_data.MatchRepository
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetCurrentTourUsecase
import javax.inject.Inject

class GetCurrentTourUsecaseImplementation @Inject constructor
    (private val matchRepository: MatchRepository) : GetCurrentTourUsecase {
    override suspend fun execute(season: Int): ResultEvent<Int> {
       return matchRepository.getTour(season)
    }
}