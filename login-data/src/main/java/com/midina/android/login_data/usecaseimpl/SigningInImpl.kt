package com.midina.android.login_data.usecaseimpl
import com.midina.android.login_data.SigningInRepository
import com.midina.login_domain.usecase.SigningIn
import javax.inject.Inject

class SigningInImpl @Inject constructor
    (private val signingInRepository: SigningInRepository): SigningIn {
    override suspend fun execute(email: String, password: String): Boolean =
        signingInRepository.getIsSigned(email, password)
}

