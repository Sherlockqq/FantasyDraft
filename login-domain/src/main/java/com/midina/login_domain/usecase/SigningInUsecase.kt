package com.midina.login_domain.usecase

import com.midina.login_domain.model.ResultEvent

interface SigningInUsecase {
    suspend fun execute(email: String, password: String): ResultEvent<String>
}