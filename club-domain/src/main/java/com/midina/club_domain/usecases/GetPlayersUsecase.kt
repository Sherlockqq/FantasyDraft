package com.midina.club_domain.usecases

import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.player.Player

interface GetPlayersUsecase {
    suspend fun execute(teamId: Int, season: Int): ResultEvent<List<Player>>
}