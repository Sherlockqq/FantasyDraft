package com.midina.android.match_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.android.match_ui.MatchFragment
import com.midina.android.match_ui.MatchViewModel
import com.midina.core_ui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface MatchUiModule {

    @ContributesAndroidInjector(
        modules = [
            MatchViewModelModule::class,
            BundleProvidersModule::class]
    )

    fun providesMatchFragment(): MatchFragment

}

@Module
abstract class MatchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    abstract fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel
}

@Module
class BundleProvidersModule {

    @Provides
    fun provideBundle(matchFragment: MatchFragment): Bundle? {
        return matchFragment.arguments
    }
}