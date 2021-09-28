package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegistrUserRepository
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegistrUser
import javax.inject.Inject

class RegistrUserImpl @Inject constructor(
    private val registrUserRepository: RegistrUserRepository) : RegistrUser {
    override suspend fun execute(email: String, password: String): ResultEvent =
        registrUserRepository.getIsRegistered(email, password)
}