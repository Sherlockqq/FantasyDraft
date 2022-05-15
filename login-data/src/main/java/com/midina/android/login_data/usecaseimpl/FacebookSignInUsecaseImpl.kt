package com.midina.android.login_data.usecaseimpl

import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.FacebookSignInUsecase
import javax.inject.Inject

class FacebookSignInUsecaseImpl @Inject constructor
    (private val signingInRepository: SigningInRepository) : FacebookSignInUsecase {
    override suspend fun execute(token: String): ResultEvent<String> {
        return signingInRepository.facebookSignIn(token)
    }
}