package com.midina.draft_data.usecaseimpl

import com.midina.draft_data.SignedRepository
import com.midina.draft_domain.model.ResultSending
import com.midina.draft_domain.usecase.SendingVerificationUsecase
import javax.inject.Inject

class SendingVerificationUsecaseImpl @Inject constructor
    (private val signedRepository: SignedRepository) : SendingVerificationUsecase {
    override suspend fun execute(): ResultSending {
       return  signedRepository.sendVerification()
    }
}