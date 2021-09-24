package com.midina.login_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.login_ui.LoginFragment
import com.midina.login_ui.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface LoginUiModule {

    @ContributesAndroidInjector(
        modules = [
            LoginViewModelModule::class
        ]
    )
    fun providesLoginFragment(): LoginFragment
}

@Module
abstract class LoginViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: LoginViewModel): ViewModel
}