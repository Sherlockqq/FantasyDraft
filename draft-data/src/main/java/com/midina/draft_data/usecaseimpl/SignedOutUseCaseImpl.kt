package com.midina.draft_data.usecaseimpl

import com.midina.draft_data.SignedRepository
import com.midina.draft_domain.usecase.SignedOutUseCase
import javax.inject.Inject

class SignedOutUseCaseImpl @Inject constructor
    (private val signedRepository: SignedRepository) : SignedOutUseCase {
    override suspend fun execute() = signedRepository.signedOut()
}

