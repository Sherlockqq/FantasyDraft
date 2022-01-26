package com.midina.favourite_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.favourite_ui.FavouriteFragment
import com.midina.favourite_ui.FavouriteViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
interface FavouriteUiModule {

    @ContributesAndroidInjector(
        modules = [
            FavouriteViewModelModule::class
        ]
    )
    fun providesSplashFragment(): FavouriteFragment
}

@Module
abstract class FavouriteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel::class)
    abstract fun bindFixturesViewModel(viewModel: FavouriteViewModel): ViewModel
}