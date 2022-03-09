package com.midina.registration_domain.usecase

import com.midina.registration_domain.model.ResultEvent

interface RegisterUserUsecase {
    suspend fun execute(email: String, password: String): ResultEvent<String>
}