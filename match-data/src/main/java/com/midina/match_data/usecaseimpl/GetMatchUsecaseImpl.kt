package com.midina.match_data.usecaseimpl

import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.model.Match
import com.midina.android.match_domain.usecase.GetMatchUsecase
import com.midina.match_data.repositories.MatchRepository
import javax.inject.Inject

class GetMatchUsecaseImpl @Inject constructor(private val matchRepository: MatchRepository) :
    GetMatchUsecase {
    override suspend fun execute(matchId: Int): ResultEvent<Match> =
        matchRepository.getMatch(matchId)
}