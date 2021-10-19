package com.midina.matches_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.matches_ui.FixturesFragment
import com.midina.matches_ui.fixtures.FixturesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface MatchesUiModule {

    @ContributesAndroidInjector(
        modules = [
            FixturesViewModelModule::class
        ]
    )
    fun providesFixturesFragment(): FixturesFragment
}

@Module
abstract class FixturesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FixturesViewModel::class)
    abstract fun bindFixturesViewModel(viewModel: FixturesViewModel): ViewModel
}