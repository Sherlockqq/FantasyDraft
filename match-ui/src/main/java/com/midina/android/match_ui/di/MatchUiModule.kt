package com.midina.android.match_ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.midina.android.match_ui.MatchFragment
import com.midina.android.match_ui.MatchViewModel
import com.midina.android.match_ui.MatchViewModelFactory
import com.midina.core_ui.di.ViewModelFactory
import com.midina.core_ui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Scope
import javax.inject.Singleton

@Module
interface MatchUiModule {

    @ContributesAndroidInjector(modules = [
        MatchViewModelModule::class,
        MatchViewModelBuilderModule::class,
        BundleProvidersModule::class])

    fun providesMatchFragment(): MatchFragment// =  matchFragment

}
@Module
abstract class MatchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    abstract fun bindMatchViewModel(viewModel: MatchViewModel): ViewModel
}

@Module
abstract class MatchViewModelBuilderModule {
    @Binds
    internal abstract fun bindMatchViewModelFactory(
        factory: MatchViewModelFactory
    ): ViewModelFactory
}

@Module
class BundleProvidersModule {

    @Provides
    @Singleton
    @Named("HomeTeam")
    fun provideHomeTeam(matchFragment: MatchFragment): String? {
        val bundle = matchFragment.arguments
        return bundle?.getString("HomeTeam")
    }
}