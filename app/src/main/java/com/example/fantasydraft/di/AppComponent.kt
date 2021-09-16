package com.example.fantasydraft.di

import android.content.Context
import com.example.fantasydraft.FantasydraftApplication
import com.midina.core_ui.di.ViewModelBuilderModule
import com.midina.matches_data.di.MatchesDataModule
import com.midina.matches_ui.di.MatchesUiModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import dagger.android.AndroidInjectionModule

@Singleton
@Component (modules = [
    AndroidInjectionModule::class,
    ViewModelBuilderModule::class,
    MatchesDataModule::class,
    MatchesUiModule::class,
    MainActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun create(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(application: FantasydraftApplication)
}