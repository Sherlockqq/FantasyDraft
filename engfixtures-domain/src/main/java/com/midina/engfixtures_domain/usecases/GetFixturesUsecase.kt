package com.midina.engfixtures_domain.usecases

import com.midina.engfixtures_domain.model.Match
import com.midina.engfixtures_domain.model.ResultEvent

interface GetFixturesUsecase {
    suspend fun execute(): ResultEvent<Map<Int, ArrayList<Match>>>
}