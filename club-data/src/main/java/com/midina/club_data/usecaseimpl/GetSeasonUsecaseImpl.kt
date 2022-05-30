package com.midina.club_data.usecaseimpl

import com.midina.club_data.ClubRepository
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.usecases.GetSeasonUsecase
import javax.inject.Inject

class GetSeasonUsecaseImpl @Inject constructor(val clubRepository: ClubRepository) :
    GetSeasonUsecase {
    override suspend fun execute(): ResultEvent<Int> {
        return try {
            val leaguePref = clubRepository.getLeaguePreferences()

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