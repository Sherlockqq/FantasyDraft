package com.midina.match_data.di

import com.midina.android.match_domain.usecase.GetWeatherUsecase
import com.midina.match_data.api.WeatherApiInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

//api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}

const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

@Module(
    includes = [
        MatchDataUseCaseModule::class
    ]
)
class MatchDataModule {

    @Singleton
    @Provides
    @Named("Weather")
    fun getRetrofitInstance(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getWeatherApiInterface(@Named("Weather")retrofit: Retrofit): WeatherApiInterface {
        return retrofit.create(WeatherApiInterface::class.java)
    }
}

@Module
interface MatchDataUseCaseModule {
    @Binds
    fun bindGetMatchWeatherUseCase(getWeatherImpl: com.midina.match_data.GetWeatherUsecaseImpl): GetWeatherUsecase
}