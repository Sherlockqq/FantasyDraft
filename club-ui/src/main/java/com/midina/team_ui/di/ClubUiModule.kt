package com.midina.team_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.team_ui.club.ClubFragment
import com.midina.team_ui.club.ClubViewModel
import com.midina.team_ui.player.PlayersPageFragment
import com.midina.team_ui.player.PlayersPageViewModel
import com.midina.team_ui.stat.StatisticPageFragment
import com.midina.team_ui.stat.StatisticPageViewModel
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
            ClubViewModelModule::class,
            BundleProvidersClubModule::class
        ]
    )
    fun providesClubFragment(): ClubFragment


    @ContributesAndroidInjector(
        modules = [
            StatisticPageViewModelModule::class]
    )
    fun providesStatisticPageFragment(): StatisticPageFragment

    @ContributesAndroidInjector(
        modules = [
            PlayerPageViewModelModule::class,
            BundleProvidersClubModule::class
        ]
    )
    fun providesPlayersPageFragment(): PlayersPageFragment

}

@Module
abstract class ClubViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ClubViewModel::class)
    abstract fun bindClubViewModel(viewModel: ClubViewModel): ViewModel
}

@Module
abstract class PlayerPageViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlayersPageViewModel::class)
    abstract fun bindPlayerPageViewModel(viewModel: PlayersPageViewModel): ViewModel
}

@Module
abstract class StatisticPageViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StatisticPageViewModel::class)
    abstract fun bindPlayerPageViewModel(viewModel: StatisticPageViewModel): ViewModel
}

@Module
class BundleProvidersClubModule {

    @Named("ClubBundle")
    @Provides
    fun provideClubBundle(clubFragment: ClubFragment): Bundle? {
        return clubFragment.arguments
    }

    @Named("PlayerBundle")
    @Provides
    fun providePlayerBundle(playersPageFragment: PlayersPageFragment): Bundle? {
        return playersPageFragment.arguments
    }
}