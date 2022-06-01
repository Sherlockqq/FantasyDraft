package com.midina.login_domain.usecase

import com.midina.login_domain.model.ResultEvent

interface FacebookSignInUsecase {
    suspend fun execute(token: String) : ResultEvent<String>
}