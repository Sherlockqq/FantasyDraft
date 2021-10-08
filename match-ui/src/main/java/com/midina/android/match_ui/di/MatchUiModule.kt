package com.midina.android.match_ui.di

import androidx.lifecycle.ViewModel
import com.midina.android.match_ui.MatchFragment
import com.midina.android.match_ui.MatchViewModel
import com.midina.core_ui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
interface MatchUiModule {

    @ContributesAndroidInjector(modules = [MatchViewModelModule::class])

    fun providesMatchFragment(): MatchFragment
}
@Module
abstract class MatchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    abstract fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel
}