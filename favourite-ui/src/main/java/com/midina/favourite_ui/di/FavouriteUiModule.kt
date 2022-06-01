package com.midina.favourite_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.favourite_ui.FavouriteFragment
import com.midina.favourite_ui.FavouriteViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module
interface FavouriteUiModule {

    @ContributesAndroidInjector(
        modules = [
            FavouriteViewModelModule::class,
            BundleProvidersSplashModule::class
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

@Module
class BundleProvidersSplashModule {

    @Named("FavouriteBundle")
    @Provides
    fun provideBundle(favouriteFragment: FavouriteFragment): Bundle? {
        return favouriteFragment.arguments
    }
}