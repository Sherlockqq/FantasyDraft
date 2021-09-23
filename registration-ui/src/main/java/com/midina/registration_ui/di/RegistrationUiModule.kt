package com.midina.registration_ui.di

import com.midina.registration_ui.RegistrationFragment
import dagger.Module

@Module
interface RegistrationUiModule {

    fun providesRegistrationFragment(): RegistrationFragment
}