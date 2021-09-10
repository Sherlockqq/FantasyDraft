package com.example.fantasydraft.di

import com.midina.matches_data.di.MatchesDataModule
import dagger.Component

@Component (modules = [AppModule::class, MatchesDataModule::class])
interface AppComponent