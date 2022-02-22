package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.model.User
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import javax.inject.Inject

class WriteToDatabaseUsecaseImpl @Inject constructor(
    private val registerUserRepository: RegisterUserRepository
) : WriteToDatabaseUsecase {
    override suspend fun execute(user: User): ResultEvent {
        return registerUserRepository.writeToDatabase(user)
    }
}