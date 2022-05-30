package com.midina.team_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.team_ui.ClubFragment
import com.midina.team_ui.ClubViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
interface ClubUiModule {

    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class,
            BundleProvidersClubModule::class
        ]
    )
    fun providesClubFragment(): ClubFragment
}

@Module
abstract class SplashViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ClubViewModel::class)
    abstract fun bindClubViewModel(viewModel: ClubViewModel): ViewModel
}

@Module
class BundleProvidersClubModule {

    @Named("ClubBundle")
    @Provides
    fun provideBundle(clubFragment: ClubFragment): Bundle? {
        return clubFragment.arguments
    }
}