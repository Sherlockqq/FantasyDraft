package com.midina.matches_data.usecaseimpl

import com.midina.matches_data.MatchRepository
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import javax.inject.Inject

class GetMatchesScheduleUsecaseImpl @Inject constructor
    (private val matchRepository: MatchRepository) : GetMatchesScheduleUsecase {
    override suspend fun execute(): ResultEvent<Map<Int, ArrayList<MatchSchedule>>> =
        matchRepository.getMatchMap()
}