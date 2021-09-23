package com.midina.login_domain.usecase

interface SigningIn {
    suspend fun execute(email: String, password: String): Boolean
}