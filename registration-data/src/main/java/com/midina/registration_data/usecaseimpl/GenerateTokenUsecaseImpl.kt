package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.GenerateTokenUsecase
import javax.inject.Inject

class GenerateTokenUsecaseImpl @Inject constructor(
    private val registerUserRepository: RegisterUserRepository
) : GenerateTokenUsecase {
    override suspend fun execute(): ResultEvent<String> {
        return registerUserRepository.generateMessagingToken()
    }
}