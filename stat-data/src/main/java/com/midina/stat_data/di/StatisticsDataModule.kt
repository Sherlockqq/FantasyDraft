package com.midina.stat_data.di

import com.midina.stat_data.api.StatisticsApiInterface
import com.midina.stat_data.usecaseimpl.GetSeasonUsecaseImpl
import com.midina.stat_domain.GetSeasonUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "https://v3.football.api-sports.io/"

@Module(
    includes = [
        StatisticsDataUseCaseModule::class
    ]
)
class StatisticsDataModule {

    @Singleton
    @Provides
    @Named("Season")
    fun getRetrofitInstance(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getStatisticsApiInterface(@Named("Season")retrofit: Retrofit): StatisticsApiInterface {
        return retrofit.create(StatisticsApiInterface::class.java)
    }
}

@Module
interface StatisticsDataUseCaseModule {
    @Binds
    fun bindSeasonImpl(getSeason: GetSeasonUsecaseImpl): GetSeasonUseCase
}