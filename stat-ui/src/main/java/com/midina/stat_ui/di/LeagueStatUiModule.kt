package com.midina.stat_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.stat_ui.LeagueStatFragment
import com.midina.stat_ui.LeagueStatViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface LeagueStatUiModule {

    @ContributesAndroidInjector(
        modules = [
            LeagueStatViewModelModule::class
        ]
    )
    fun providesLeagueStatFragment(): LeagueStatFragment


}

@Module
abstract class LeagueStatViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LeagueStatViewModel::class)
    abstract fun bindLeagueStatViewModel(viewModel: LeagueStatViewModel): ViewModel
}