package com.midina.android.login_data.usecaseimpl

import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.model.EmailResult
import com.midina.login_domain.usecase.CheckEmailExistUsecase
import javax.inject.Inject

class CheckEmailExistUsecaseImpl @Inject constructor
    (private val signingInRepository: SigningInRepository) : CheckEmailExistUsecase {
    override suspend fun execute(email: String): EmailResult {
        return signingInRepository.checkEmailExist(email)
    }
}