package com.midina.draft_data.di

import com.midina.draft_data.SignedRepository
import com.midina.draft_data.usecaseimpl.IsSignedImpl
import com.midina.draft_data.usecaseimpl.SignedOutImpl
import com.midina.draft_domain.usecase.IsSigned
import com.midina.draft_domain.usecase.SignedOut
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    DraftDataUseCaseModule::class
])
class DraftDataModule {
    @Provides
    @Singleton
    fun provideIsSignedRepository() = SignedRepository()
}

@Module
interface DraftDataUseCaseModule {
    @Binds
    fun bindIsSignedImpl(isSignedImpl: IsSignedImpl): IsSigned
    @Binds
    fun bindSignedOutImpl(signedOutImpl: SignedOutImpl): SignedOut
}