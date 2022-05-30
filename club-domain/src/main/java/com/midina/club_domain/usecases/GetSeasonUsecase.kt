package com.midina.club_domain.usecases

import com.midina.club_domain.model.ResultEvent

interface GetSeasonUsecase {
    suspend fun execute(): ResultEvent<Int>
}