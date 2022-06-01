package com.midina.core_domain.usecases

import com.midina.core_domain.model.ResultEvent

interface GetSeasonUsecase {
    suspend fun execute(): ResultEvent<Int>
}