package com.midina.registration_domain.usecase

import com.midina.registration_domain.model.ResultEvent

interface GenerateTokenUsecase {
    suspend fun execute() : ResultEvent<String>
}