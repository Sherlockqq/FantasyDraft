package com.midina.match_data.di

import com.midina.android.match_domain.usecase.GetMatchUsecase
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import com.midina.match_data.api.MatchApiInterface
import com.midina.match_data.usecaseimpl.GetWeatherUsecaseImpl
import com.midina.match_data.api.WeatherApiInterface
import com.midina.match_data.usecaseimpl.GetMatchUsecaseImpl
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

//api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}

const val WEATHER_URL = "http://api.openweathermap.org/data/2.5/"
const val FOOTBALL_URL = "https://v3.football.api-sports.io"

@Module(
    includes = [
        MatchDataUseCaseModule::class
    ]
)
class MatchDataModule {

    @Singleton
    @Provides
    @Named("Weather")
    fun getWeatherRetrofitInstance(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getWeatherApiInterface(@Named("Weather")retrofit: Retrofit): WeatherApiInterface {
        return retrofit.create(WeatherApiInterface::class.java)
    }


    @Singleton
    @Provides
    @Named("Match")
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
    fun getFootballApiInterface(@Named("Match")retrofit: Retrofit): MatchApiInterface {
        return retrofit.create(MatchApiInterface::class.java)
    }

}

@Module
interface MatchDataUseCaseModule {
    @Binds
    fun bindGetMatchWeatherUseCase(getWeatherImpl: GetWeatherUsecaseImpl): GetWeatherUsecase

    @Binds
    fun bindGetVenueUseCase(getMatchUsecaseImpl: GetMatchUsecaseImpl): GetMatchUsecase
}