package com.midina.android.login_data.usecaseimpl
import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.SigningInUsecase
import javax.inject.Inject

class SigningInUsecaseImpl @Inject constructor
    (private val signingInRepository: SigningInRepository): SigningInUsecase {
    override suspend fun execute(email: String, password: String): ResultEvent<String> =
        signingInRepository.getIsSigned(email, password)
}

