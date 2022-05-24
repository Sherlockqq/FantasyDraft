package com.midina.android.match_domain.usecase

import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.model.Match

interface GetMatchUsecase {
    suspend fun execute(matchId: Int): ResultEvent<Match>
}