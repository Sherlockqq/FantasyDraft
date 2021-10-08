package com.example.fantasydraft.di

import android.content.Context
import com.example.fantasydraft.FantasydraftApplication
import com.midina.android.login_data.di.LoginDataModule
import com.midina.android.match_data.di.MatchDataModule
import com.midina.android.match_ui.MatchFragment
import com.midina.android.match_ui.di.MatchUiModule
import com.midina.core_ui.di.ViewModelBuilderModule
import com.midina.draft_data.di.DraftDataModule
import com.midina.draft_ui.di.DraftUiModule
import com.midina.login_ui.di.LoginUiModule
import com.midina.matches_data.di.MatchesDataModule
import com.midina.matches_ui.di.MatchesUiModule
import com.midina.registration_data.di.RegistrationDataModule
import com.midina.registration_ui.di.RegistrationUiModule
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
    MainActivityModule::class,
    DraftUiModule::class,
    DraftDataModule::class,
    RegistrationUiModule::class,
    RegistrationDataModule::class,
    LoginUiModule::class,
    LoginDataModule::class,
    MatchUiModule::class,
    MatchDataModule::class])

interface AppComponent {
    @Component.Builder
    interface Builder {
        fun create(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(application: FantasydraftApplication)
}