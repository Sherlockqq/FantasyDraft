package com.midina.login_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.login_ui.LoginFragment
import com.midina.login_ui.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
interface LoginUiModule {

    @ContributesAndroidInjector(
        modules = [
            LoginViewModelModule::class,
            LoginDispatchersModule::class
        ]
    )
    fun providesLoginFragment(): LoginFragment


}

@Module
class LoginDispatchersModule {
    @Provides
    fun provideIoDispatchers() = Dispatchers.IO
}

@Module
abstract class LoginViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(com.midina.login_ui.LoginViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: LoginViewModel): ViewModel
}