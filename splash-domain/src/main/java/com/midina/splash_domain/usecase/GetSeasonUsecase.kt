package com.midina.splash_domain.usecase

import com.midina.splash_domain.model.ResultEvent

interface GetSeasonUsecase {
    suspend fun execute(): ResultEvent<Int>
}