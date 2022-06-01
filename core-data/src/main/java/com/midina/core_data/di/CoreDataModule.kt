package com.midina.core_data.di

import com.midina.core_data.api.CoreApiInterface
import com.midina.core_data.usecaseimpl.GetSeasonUsecaseImpl
import com.midina.core_domain.usecases.GetSeasonUsecase
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

const val FOOTBALL_URL = "https://v3.football.api-sports.io"

@Module(
    includes = [
        CoreDataUseCaseModule::class
    ]
)
class CoreDataModule {

    @Singleton
    @Provides
    @Named("Core")
    fun getCoreRetrofitInstance(): Retrofit {

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
    fun getFootballApiInterface(@Named("Core")retrofit: Retrofit): CoreApiInterface {
        return retrofit.create(CoreApiInterface::class.java)
    }

}

@Module
interface CoreDataUseCaseModule {
    @Binds
    fun bindGetSeasonUseCase(getSeasonUsecaseImpl: GetSeasonUsecaseImpl): GetSeasonUsecase
}