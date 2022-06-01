package com.midina.matches_data.usecaseimpl

import com.midina.matches_data.FixturesRepository
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetCurrentTourUsecase
import javax.inject.Inject

class GetCurrentTourUsecaseImplementation @Inject constructor
    (private val fixturesRepository: FixturesRepository) : GetCurrentTourUsecase {
    override suspend fun execute(season: Int): ResultEvent<Int> {
        return try {
            val currentTourData = fixturesRepository.getTour(season)

            if (currentTourData.response.isEmpty()) {
                ResultEvent.Success(0)
            } else {
                ResultEvent.Success(
                    currentTourData.response[0]
                        .takeLast(2)
                        .trim()
                        .toInt()
                )
            }
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }
}