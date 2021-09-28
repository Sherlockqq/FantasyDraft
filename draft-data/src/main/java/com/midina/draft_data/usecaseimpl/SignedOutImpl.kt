package com.midina.draft_data.usecaseimpl

import com.midina.draft_data.SignedRepository
import com.midina.draft_domain.usecase.SignedOut
import javax.inject.Inject

class SignedOutImpl @Inject constructor
    (private val signedRepository: SignedRepository) : SignedOut {
    override suspend fun execute() = signedRepository.signedOut()
}

