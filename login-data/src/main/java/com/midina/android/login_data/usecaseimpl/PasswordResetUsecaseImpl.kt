package com.midina.android.login_data.usecaseimpl

import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.model.PasswordResetResult
import com.midina.login_domain.usecase.PasswordResetUsecase
import javax.inject.Inject

class PasswordResetUsecaseImpl @Inject constructor
    (private val signingInRepository: SigningInRepository): PasswordResetUsecase {
    override suspend fun execute(email: String): PasswordResetResult {
        return signingInRepository.resetPassword(email)
    }
}