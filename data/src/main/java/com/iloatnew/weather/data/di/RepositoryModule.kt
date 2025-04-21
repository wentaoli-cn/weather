package com.iloatnew.weather.data.di

import com.iloatnew.weather.data.repository.location.LocationRepository
import com.iloatnew.weather.data.repository.location.LocationRepositoryImpl
import com.iloatnew.weather.data.repository.weather.WeatherRepository
import com.iloatnew.weather.data.repository.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindLocationRepository(
        locationRepository: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    fun bindWeatherRepository(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherRepository
}
