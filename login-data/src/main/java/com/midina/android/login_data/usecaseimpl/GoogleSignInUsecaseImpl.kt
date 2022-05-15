package com.midina.android.login_data.usecaseimpl

import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.GoogleSignInUsecase
import javax.inject.Inject

class GoogleSignInUsecaseImpl @Inject constructor
    (private val signingInRepository: SigningInRepository) : GoogleSignInUsecase {
    override suspend fun execute(idToken: String): ResultEvent<String> {
        return signingInRepository.googleSignIn(idToken)
    }
}