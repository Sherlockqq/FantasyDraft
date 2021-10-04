package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegisterUser
import javax.inject.Inject

class RegisterUserImpl @Inject constructor(
    private val registerUserRepository: RegisterUserRepository) : RegisterUser {
    override suspend fun execute(user: User, password: String): ResultEvent =
        registerUserRepository.getIsRegistered(user, password)
}