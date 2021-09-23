package com.midina.registration_domain.usecase

interface registrUser {
    suspend fun execute(email: String, password: String): Boolean
}