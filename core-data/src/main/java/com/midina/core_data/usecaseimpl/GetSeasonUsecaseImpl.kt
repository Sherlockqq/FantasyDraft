package com.midina.core_data.usecaseimpl

import com.midina.core_data.CoreRepository
import com.midina.core_domain.model.ResultEvent
import com.midina.core_domain.usecases.GetSeasonUsecase
import javax.inject.Inject

class GetSeasonUsecaseImpl @Inject constructor(val coreRepository: CoreRepository) :
    GetSeasonUsecase {
    override suspend fun execute(): ResultEvent<Int> {
        return try {
            val leaguePref = coreRepository.getLeaguePreferences()

            if (leaguePref.results == 0) {
                ResultEvent.EmptyState
            } else {
                ResultEvent.Success(leaguePref.response[0].seasons[0].year)
            }
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }
}