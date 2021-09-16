package com.midina.matches_data.di

import com.midina.matches_data.MatchRepository
import com.midina.matches_data.usecaseimpl.GetMatchesScheduleImpl
import com.midina.matches_domain.usecase.GetMatchesSchedule

import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    MatchesDataUseCaseModule::class
])
class MatchesDataModule {
    @Provides
    @Singleton
    fun provideMatchRepository() = MatchRepository()
}

@Module
interface MatchesDataUseCaseModule {
    @Binds
    fun bindGetMatchesScheduleUseCase(getMatchesSchedule: GetMatchesScheduleImpl): GetMatchesSchedule
}

