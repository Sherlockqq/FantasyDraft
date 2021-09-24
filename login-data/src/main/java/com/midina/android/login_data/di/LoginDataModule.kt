package com.midina.android.login_data.di

import com.midina.android.login_data.SigningInRepository
import com.midina.android.login_data.usecaseimpl.SigningInImpl
import com.midina.login_domain.usecase.SigningIn
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    LoginDataUseCaseModule::class
])
class LoginDataModule {
    @Provides
    @Singleton
    fun provideSigningInRepository() = SigningInRepository()
}

@Module
interface LoginDataUseCaseModule {
    @Binds
    fun bindSigningInImpl(registrUser: SigningInImpl): SigningIn
}