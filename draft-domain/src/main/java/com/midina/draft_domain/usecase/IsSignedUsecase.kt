package com.midina.draft_domain.usecase

import com.midina.draft_domain.model.ResultEvent

interface IsSignedUsecase {
    suspend fun execute(): ResultEvent
}