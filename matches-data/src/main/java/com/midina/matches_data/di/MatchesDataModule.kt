package com.midina.matches_data.di

import com.midina.matches_data.MatchRepository
import com.midina.matches_data.usecaseimpl.GetMatchesScheduleUsecaseImpl
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        MatchesDataUseCaseModule::class
    ]
)
class MatchesDataModule {
    @Provides
    @Singleton
    fun provideMatchRepository() = MatchRepository()

}

@Module
interface MatchesDataUseCaseModule {
    @Binds
    fun bindGetMatchesScheduleUseCase(getMatchesScheduleImpl: GetMatchesScheduleUsecaseImpl):
            GetMatchesScheduleUsecase
}

