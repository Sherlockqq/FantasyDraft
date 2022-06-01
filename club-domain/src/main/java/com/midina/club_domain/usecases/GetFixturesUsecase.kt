package com.midina.club_domain.usecases

import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.fixtures.FixturesInfo

interface GetFixturesUsecase {
    suspend fun execute(teamId: Int, season: Int): ResultEvent<ArrayList<FixturesInfo>>
}