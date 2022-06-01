package com.midina.splash_data.di

import com.midina.splash_data.SplashRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module()
class SplashDataModule {

    @Provides
    @Singleton
    fun provideMatchRepository() = SplashRepository()

}
