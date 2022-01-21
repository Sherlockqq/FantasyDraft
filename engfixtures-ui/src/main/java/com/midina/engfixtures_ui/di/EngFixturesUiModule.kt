package com.midina.engfixtures_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.engfixtures_ui.EngFixturesFragment
import com.midina.engfixtures_ui.EngFixturesViewModel
import com.midina.engfixtures_ui.TourFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap



@Module
interface EngFixturesUiModule {

    @ContributesAndroidInjector(
        modules = [
            EngFixturesViewModelModule::class
        ]
    )
    fun providesFixturesFragment(): EngFixturesFragment

    @ContributesAndroidInjector(
        modules = [
            EngFixturesViewModelModule::class
        ]
    )
    fun providesTourFragment(): TourFragment

}

@Module
abstract class EngFixturesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EngFixturesViewModel::class)
    abstract fun bindFixturesViewModel(viewModel: EngFixturesViewModel): ViewModel
}

