package com.midina.registration_domain.usecase

import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent

interface RegisterUserUsecase {
    suspend fun execute(user: User, password: String): ResultEvent
}