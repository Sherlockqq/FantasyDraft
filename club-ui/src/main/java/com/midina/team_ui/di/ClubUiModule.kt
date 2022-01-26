package com.midina.team_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.team_ui.ClubFragment
import com.midina.team_ui.ClubViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ClubUiModule {

    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class
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