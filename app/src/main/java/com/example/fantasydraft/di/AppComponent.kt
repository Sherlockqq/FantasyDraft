package com.example.fantasydraft.di

import android.content.Context
import com.example.fantasydraft.FantasydraftApplication
import com.midina.android.login_data.di.LoginDataModule
import com.midina.core_ui.di.ViewModelBuilderModule
import com.midina.draft_data.di.DraftDataModule
import com.midina.draft_ui.di.DraftUiModule
import com.midina.favourite_ui.di.FavouriteUiModule
import com.midina.login_ui.di.LoginUiModule
import com.midina.match_data.di.MatchDataModule
import com.midina.match_ui.di.MatchUiModule
import com.midina.matches_data.di.MatchesDataModule
import com.midina.matches_ui.di.MatchesUiModule
import com.midina.registration_data.di.RegistrationDataModule
import com.midina.registration_ui.di.RegistrationUiModule
import com.midina.splash_ui.di.SplashUiModule
import com.midina.stat_data.di.StatisticsDataModule
import com.midina.stat_ui.di.LeagueStatUiModule
import com.midina.team_ui.di.ClubUiModule
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
    com.midina.registration_data.di.RegistrationDataModule::class,
    LoginUiModule::class,
    LoginDataModule::class,
    MatchUiModule::class,
    MatchDataModule::class,
    LeagueStatUiModule::class,
    StatisticsDataModule::class,
    SplashUiModule::class,
    FavouriteUiModule::class,
    ClubUiModule::class])

interface AppComponent {
    @Component.Builder
    interface Builder {
        fun create(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(application: FantasydraftApplication)
}