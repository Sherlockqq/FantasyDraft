package com.midina.draft_domain.usecase

import com.midina.draft_domain.model.ResultEvent

interface IsSigned{
    suspend fun execute(): ResultEvent
}