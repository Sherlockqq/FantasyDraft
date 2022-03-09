package com.midina.login_domain.usecase

import com.midina.login_domain.model.ResultEvent

interface GoogleSignInUsecase {
    suspend fun execute(idToken: String): ResultEvent
}