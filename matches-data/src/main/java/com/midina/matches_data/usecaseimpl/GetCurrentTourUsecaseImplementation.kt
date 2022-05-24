package com.midina.matches_data.usecaseimpl

import com.midina.matches_data.FixturesRepository
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetCurrentTourUsecase
import javax.inject.Inject

class GetCurrentTourUsecaseImplementation @Inject constructor
    (private val fixturesRepository: FixturesRepository) : GetCurrentTourUsecase {
    override suspend fun execute(season: Int): ResultEvent<Int> {
       return fixturesRepository.getTour(season)
    }
}