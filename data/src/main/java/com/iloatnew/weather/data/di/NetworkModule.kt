package com.iloatnew.weather.data.di

import com.iloatnew.weather.data.BuildConfig
import com.iloatnew.weather.data.datasource.remote.weather.WeatherApi
import com.iloatnew.weather.data.model.intercepter.WeatherApiExceptionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.time.Duration

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val CONNECT_TIMEOUT = Duration.ofSeconds(30)
    private val WRITE_TIMEOUT = Duration.ofSeconds(30)
    private val READ_TIMEOUT = Duration.ofSeconds(30)

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                connectTimeout(CONNECT_TIMEOUT)
                writeTimeout(WRITE_TIMEOUT)
                readTimeout(READ_TIMEOUT)
                addInterceptor(WeatherApiExceptionInterceptor())
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor { message ->
                        Timber.tag("OkHttpClient").d(message)
                    }.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()

    @Provides
    fun provideWeatherRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit
            .create(WeatherApi::class.java)
}
