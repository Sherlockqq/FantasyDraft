package com.midina.engfixtures_data.usecaseimpl

import com.midina.engfixtures_data.FixturesRepository
import com.midina.engfixtures_domain.model.Match
import com.midina.engfixtures_domain.model.ResultEvent
import com.midina.engfixtures_domain.usecases.GetFixturesUsecase

import javax.inject.Inject

class GetFixturesUsecaseImpl  @Inject constructor
    (private val fixtureRepository: FixturesRepository) : GetFixturesUsecase {
    override suspend fun execute() : ResultEvent<Map<Int, ArrayList<Match>>> {
        return fixtureRepository.getFixtures()
    }
}
