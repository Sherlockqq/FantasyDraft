package com.midina.login_domain.usecase

import com.midina.login_domain.model.EmailResult

interface CheckEmailExistUsecase {
    suspend fun execute(email: String): EmailResult
}