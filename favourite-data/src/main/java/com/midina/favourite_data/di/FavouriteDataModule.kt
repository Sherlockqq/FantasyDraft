package com.midina.favourite_data.di

import com.midina.favourite_data.FavouriteRepository
import com.midina.favourite_data.api.FavouriteApiInterface
import com.midina.favourite_data.usecasesimpl.GetTeamsUsecaseImpl
import com.midina.favourite_domain.usecases.GetTeamsUsecase
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
        FavouriteDataUseCaseModule::class
    ]
)
class FavouriteDataModule {

    @Provides
    @Singleton
    fun provideMatchRepository() = FavouriteRepository(getFootballApiInterface(getRetrofitInstance()))

    @Singleton
    @Provides
    @Named("Favourite")
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
    fun getFootballApiInterface(@Named("Favourite")retrofit: Retrofit): FavouriteApiInterface {
        return retrofit.create(FavouriteApiInterface::class.java)
    }
}

@Module
interface FavouriteDataUseCaseModule {
    @Binds
    fun bindGetTeamsUseCase(getTeamsUsecaseImpl: GetTeamsUsecaseImpl): GetTeamsUsecase
}

