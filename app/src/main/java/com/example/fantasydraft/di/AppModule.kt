package com.example.fantasydraft.di

import androidx.lifecycle.ViewModel
import com.example.fantasydraft.MainActivity
import com.example.fantasydraft.MainActivityViewModel
import com.midina.core_ui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [
        MainActivityViewModelModule::class
    ])
    abstract fun providesMainActivity(): MainActivity

}

@Module
interface MainActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun postMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

}