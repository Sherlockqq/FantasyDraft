package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegisterUserUsecase
import javax.inject.Inject

class RegisterUserUsecaseImpl @Inject constructor(
    private val registerUserRepository: RegisterUserRepository
) : RegisterUserUsecase {
    override suspend fun execute(user: User, password: String): ResultEvent =
        registerUserRepository.getIsRegistered(user, password)
}