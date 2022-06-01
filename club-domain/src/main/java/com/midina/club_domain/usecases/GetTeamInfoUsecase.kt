package com.midina.club_domain.usecases

import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.team.TeamInfo

interface GetTeamInfoUsecase {
    suspend fun execute(teamId: Int): ResultEvent<TeamInfo>
}