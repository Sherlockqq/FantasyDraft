package com.midina.match_ui.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.midina.match_ui.MatchViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.match_ui.MatchFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
interface MatchUiModule {

    @ContributesAndroidInjector(
        modules = [
            MatchViewModelModule::class,
            BundleProvidersMatchModule::class,
            MatchDispatchersModule::class]
    )
    fun providesMatchFragment(): MatchFragment
}

@Module
class MatchDispatchersModule {
    @Provides
    fun provideIoDispatchers() = Dispatchers.IO
}

@Module
abstract class MatchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    abstract fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel
}

@Module
class BundleProvidersMatchModule {

    @Named("MatchBundle")
    @Provides
    fun provideBundle(matchFragment: MatchFragment): Bundle? {
        return matchFragment.arguments
    }
}