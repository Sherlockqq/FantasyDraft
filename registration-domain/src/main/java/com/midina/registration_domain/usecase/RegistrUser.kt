package com.midina.registration_domain.usecase

import com.midina.registration_domain.model.ResultEvent

interface RegistrUser {
    suspend fun execute(email: String, password: String): ResultEvent
}