package com.midina.club_data.di

import com.midina.club_data.api.ClubApiInterface
import com.midina.club_data.usecaseimpl.GetFixturesUsecaseImpl
import com.midina.club_data.usecaseimpl.GetPlayersUsecaseImpl
import com.midina.club_data.usecaseimpl.GetTeamInfoUsecaseImpl
import com.midina.club_domain.usecases.GetFixturesUsecase
import com.midina.club_domain.usecases.GetPlayersUsecase
import com.midina.club_domain.usecases.GetTeamInfoUsecase
import com.midina.core_data.di.FOOTBALL_URL
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

@Module(
    includes = [
        ClubDataUseCaseModule::class
    ]
)
class ClubDataModule {

    @Singleton
    @Provides
    @Named("Club")
    fun getMatchRetrofitInstance(): Retrofit {

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
            .baseUrl(FOOTBALL_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getFootballApiInterface(@Named("Club")retrofit: Retrofit): ClubApiInterface {
        return retrofit.create(ClubApiInterface::class.java)
    }

}

@Module
interface ClubDataUseCaseModule {
    @Binds
    fun bindGetTeamInfoUseCase(getTeamInfoUsecaseImpl: GetTeamInfoUsecaseImpl): GetTeamInfoUsecase

    @Binds
    fun bindGetFixturesUseCase(getFixturesUsecaseImpl: GetFixturesUsecaseImpl): GetFixturesUsecase

    @Binds
    fun bindGetPlayersUseCase(getPlayersUsecaseImpl: GetPlayersUsecaseImpl): GetPlayersUsecase
}