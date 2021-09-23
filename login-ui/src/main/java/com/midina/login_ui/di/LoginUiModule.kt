package com.midina.login_ui.di

import com.midina.login_ui.LoginFragment
import dagger.Module

@Module
interface LoginUiModule {

    fun providesLoginFragment(): LoginFragment
}