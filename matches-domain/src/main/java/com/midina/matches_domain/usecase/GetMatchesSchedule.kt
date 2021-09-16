package com.midina.matches_domain.usecase

import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent

interface GetMatchesSchedule {
    suspend fun execute() : ResultEvent<Map<Int, List<MatchSchedule>>>
}