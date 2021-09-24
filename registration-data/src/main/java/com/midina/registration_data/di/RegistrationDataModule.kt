package com.midina.registration_data.di

import com.midina.registration_data.RegistrUserRepository
import com.midina.registration_data.usecaseimpl.RegistrUserImpl
import com.midina.registration_domain.usecase.RegistrUser
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [
    RegistrationDataUseCaseModule::class
])
class RegistrationDataModule {
    @Provides
    @Singleton
    fun provideRegisterUserRepository() = RegistrUserRepository()
}

@Module
interface RegistrationDataUseCaseModule {
    @Binds
    fun bindRegistrUserImpl(registrUser: RegistrUserImpl): RegistrUser
}
