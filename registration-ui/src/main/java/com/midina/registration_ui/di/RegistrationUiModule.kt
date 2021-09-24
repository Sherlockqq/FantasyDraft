package com.midina.registration_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.registration_ui.RegistrationFragment
import com.midina.registration_ui.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface RegistrationUiModule {

    @ContributesAndroidInjector(
        modules = [
            RegistrationViewModelModule::class
        ]
    )
    fun providesRegistrationFragment(): RegistrationFragment
}

@Module
abstract class RegistrationViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel
}