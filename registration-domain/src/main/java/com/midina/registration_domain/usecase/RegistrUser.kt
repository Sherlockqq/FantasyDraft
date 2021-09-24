package com.midina.registration_domain.usecase

interface RegistrUser {
    suspend fun execute(email: String, password: String): Boolean
}