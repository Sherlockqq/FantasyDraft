package com.midina.registration_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.registration_ui.RegistrationFragment
import com.midina.registration_ui.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers

@Module
interface RegistrationUiModule {

    @ContributesAndroidInjector(
        modules = [
            RegistrationViewModelModule::class,
            MatchDispatchersModule::class
        ]
    )
    fun providesRegistrationFragment(): RegistrationFragment

//    @ContributesAndroidInjector()
//    fun provideVerificationFragment(): VerificationFragment
}

@Module
class MatchDispatchersModule {
    @Provides
    fun provideIoDispatchers() = Dispatchers.IO
}

@Module
abstract class RegistrationViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel
}