package com.midina.matches_domain.usecases

import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent

interface GetMatchesScheduleUsecase {
    suspend fun execute(season: Int): ResultEvent<Map<Int, ArrayList<MatchSchedule>>>
}