package com.midina.login_domain.usecase

import com.midina.login_domain.model.PasswordResetResult

interface PasswordResetUsecase {
    suspend fun execute(email: String): PasswordResetResult
}