package com.midina.draft_ui.di

import androidx.lifecycle.ViewModel
import com.midina.core_ui.di.ViewModelKey
import com.midina.draft_ui.DraftFragment
import com.midina.draft_ui.DraftViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface DraftUiModule {

    @ContributesAndroidInjector(
        modules = [
            DraftViewModelModule::class
        ]
    )
    fun providesDraftFragment(): DraftFragment
}

@Module
abstract class DraftViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DraftViewModel::class)
    abstract fun bindFixturesViewModel(viewModel: DraftViewModel): ViewModel
}