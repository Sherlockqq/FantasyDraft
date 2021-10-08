package com.midina.android.match_data.di

import com.midina.android.match_data.api.WeatherApiInterface
import com.midina.android.match_data.usecaseImpl.GetWeatherImpl
import com.midina.android.match_domain.usecase.GetWeather
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


//api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}

const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

@Module(includes = [
    MatchDataUseCaseModule::class
])
class MatchDataModule {

    @Singleton
    @Provides
    fun getRetrofitInstance():Retrofit{

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //Todo ad
    }

    @Singleton
    @Provides
    fun getWeatherApiInterface(retrofit: Retrofit):WeatherApiInterface{
        return retrofit.create(WeatherApiInterface::class.java)
    }
}

@Module
interface MatchDataUseCaseModule {
    @Binds
    fun bindGetMatchWeatherUseCase(getWeatherImpl: GetWeatherImpl): GetWeather
}