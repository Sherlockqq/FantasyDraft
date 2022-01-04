package com.midina.stat_data.di

import com.midina.stat_data.api.StatisticsApiInterface
import com.midina.stat_data.usecaseimpl.GetDataUsecaseImpl
import com.midina.stat_domain.GetDataUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "https://v3.football.api-sports.io"

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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    fun bindDataImpl(getData: GetDataUsecaseImpl): GetDataUsecase
}