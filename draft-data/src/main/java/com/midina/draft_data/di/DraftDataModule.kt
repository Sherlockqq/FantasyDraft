package com.midina.draft_data.di

import com.midina.draft_data.SignedRepository
import com.midina.draft_data.usecaseimpl.IsSignedUsecaseImpl
import com.midina.draft_data.usecaseimpl.SendingVerificationUsecaseImpl
import com.midina.draft_data.usecaseimpl.SignedOutUseCaseImpl
import com.midina.draft_domain.usecase.IsSignedUsecase
import com.midina.draft_domain.usecase.SendingVerificationUsecase
import com.midina.draft_domain.usecase.SignedOutUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        DraftDataUseCaseModule::class
    ]
)
class DraftDataModule {
    @Provides
    @Singleton
    fun provideIsSignedRepository() = SignedRepository()
}

@Module
interface DraftDataUseCaseModule {
    @Binds
    fun bindIsSignedImpl(isSignedImpl: IsSignedUsecaseImpl): IsSignedUsecase

    @Binds
    fun bindSignedOutImpl(signedOutImpl: SignedOutUseCaseImpl): SignedOutUseCase

    @Binds
    fun bindSendingVerificationImpl(
        sendingVerificationImpl: SendingVerificationUsecaseImpl
    ): SendingVerificationUsecase
}