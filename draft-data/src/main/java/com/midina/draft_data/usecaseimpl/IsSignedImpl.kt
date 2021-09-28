package com.midina.draft_data.usecaseimpl

import com.midina.draft_data.SignedRepository
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.usecase.IsSigned
import javax.inject.Inject

class IsSignedImpl @Inject constructor
    (private val isSignedRepository: SignedRepository): IsSigned {
    override suspend fun execute(): ResultEvent =
        isSignedRepository.isSigned()
}


