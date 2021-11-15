package com.midina.draft_data.usecaseimpl

import com.midina.draft_data.SignedRepository
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.usecase.IsSignedUsecase
import javax.inject.Inject

class IsSignedUsecaseImpl @Inject constructor
    (private val isSignedRepository: SignedRepository): IsSignedUsecase {
    override suspend fun execute(): ResultEvent =
        isSignedRepository.isSigned()
}


