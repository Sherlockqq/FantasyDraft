package com.midina.matches_data.di

import com.midina.matches_data.FixturesRepository
import com.midina.matches_data.api.FixturesApiInterface
import com.midina.matches_data.usecaseimpl.GetCurrentTourUsecaseImplementation
import com.midina.matches_data.usecaseimpl.GetMatchesScheduleUsecaseImpl
import com.midina.matches_domain.usecases.GetCurrentTourUsecase
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "https://v3.football.api-sports.io"

@Module(
    includes = [
        MatchesDataUseCaseModule::class
    ]
)
class MatchesDataModule {

    @Provides
    @Singleton
    fun provideMatchRepository() = FixturesRepository(getFootballApiInterface(getRetrofitInstance()))

    @Singleton
    @Provides
    @Named("Fixtures")
    fun getRetrofitInstance(): Retrofit {

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
    fun getFootballApiInterface(@Named("Fixtures")retrofit: Retrofit): FixturesApiInterface {
        return retrofit.create(FixturesApiInterface::class.java)
    }
}

@Module
interface MatchesDataUseCaseModule {
    @Binds
    fun bindGetMatchesScheduleUseCase(getMatchesScheduleImpl: GetMatchesScheduleUsecaseImpl):
            GetMatchesScheduleUsecase

    @Binds
    fun bindGetСurrentTourUseCase(getCurrentTourUsecaseImpl: GetCurrentTourUsecaseImplementation):
            GetCurrentTourUsecase
}

