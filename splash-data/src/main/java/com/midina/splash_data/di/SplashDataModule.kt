package com.midina.splash_data.di

import com.midina.splash_data.SplashRepository
import com.midina.splash_data.api.SplashApiInterface
import com.midina.splash_data.usecaseimpl.GetSeasonUsecaseImpl
import com.midina.splash_domain.usecase.GetSeasonUsecase
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
        SplashDataUseCaseModule::class
    ]
)
class SplashDataModule {

    @Provides
    @Singleton
    fun provideMatchRepository() = SplashRepository(getFootballApiInterface(getRetrofitInstance()))

    @Singleton
    @Provides
    @Named("Splash")
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
    fun getFootballApiInterface(@Named("Splash")retrofit: Retrofit): SplashApiInterface {
        return retrofit.create(SplashApiInterface::class.java)
    }
}

@Module
interface SplashDataUseCaseModule {
    @Binds
    fun bindGetSeasonUseCase(getSeasonUsecaseImpl: GetSeasonUsecaseImpl): GetSeasonUsecase
}