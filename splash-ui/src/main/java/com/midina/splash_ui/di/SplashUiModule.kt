package com.midina.splash_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.splash_ui.SplashFragment
import com.midina.splash_ui.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface SplashUiModule {

    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class
        ]
    )
    fun providesSplashFragment(): SplashFragment
}

@Module
abstract class SplashViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}