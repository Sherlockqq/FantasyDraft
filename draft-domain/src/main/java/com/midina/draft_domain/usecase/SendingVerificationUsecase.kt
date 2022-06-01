package com.midina.draft_domain.usecase

import com.midina.draft_domain.model.ResultSending

interface SendingVerificationUsecase {
    suspend fun execute(): ResultSending
}