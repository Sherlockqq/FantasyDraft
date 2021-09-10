package com.midina.matches_data.di

import com.midina.matches_data.MatchRepository
import com.midina.matches_data.usecaseimpl.GetMatchesScheduleImpl

import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MatchesDataModule {
    @Provides
    @Singleton
    fun provideMatchRepository() = MatchRepository()
}

