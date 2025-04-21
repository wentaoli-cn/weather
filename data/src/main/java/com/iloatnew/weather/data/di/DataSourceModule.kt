package com.iloatnew.weather.data.di

import com.iloatnew.weather.data.datasource.local.location.LocationLocalDataSource
import com.iloatnew.weather.data.datasource.local.location.LocationLocalDataSourceImpl
import com.iloatnew.weather.data.datasource.remote.weather.WeatherRemoteDataSource
import com.iloatnew.weather.data.datasource.remote.weather.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindLocationLocalDataSource(
        locationLocalDataSource: LocationLocalDataSourceImpl
    ): LocationLocalDataSource

    @Binds
    fun bindWeatherRemoteDataSource(
        weatherRemoteDataSource: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource
}
