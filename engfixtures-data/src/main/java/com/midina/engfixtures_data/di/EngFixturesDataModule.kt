package com.midina.engfixtures_data.di

import com.midina.engfixtures_data.api.FootballApiInterface
import com.midina.engfixtures_data.usecaseimpl.GetFixturesUsecaseImpl
import com.midina.engfixtures_domain.usecases.GetFixturesUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

const val BASE_URL = "https://v3.football.api-sports.io"

@Module(
    includes = [
        MatchDataUseCaseModule::class
    ]
)
class EngFixturesDataModule {

    @Singleton
    @Provides
    @Named("Fixtures")
    fun getFootballRetrofitInstance(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
            .apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain
                    .request()
                    .newBuilder()
                    .addHeader("x-rapidapi-key", "40220fc8e6a6886380a261bcaea348c8")
                    .addHeader("x-apisports-key", "40220fc8e6a6886380a261bcaea348c8")
                    .addHeader("x-rapidapi-host","v3.football.api-sports.io")
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun getFootballApiInterface(@Named("Fixtures") retrofit: Retrofit): FootballApiInterface {
        return retrofit.create(FootballApiInterface::class.java)
    }
}

@Module
interface MatchDataUseCaseModule {
    @Binds
    fun bindGetFixturesUseCase(getFixturesImpl: GetFixturesUsecaseImpl): GetFixturesUsecase
}