package com.iloatnew.weather.domain.usecase

import com.iloatnew.weather.data.repository.location.LocationRepository
import com.iloatnew.weather.data.repository.weather.WeatherRepository
import javax.inject.Inject

class UseCaseProvider @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) {
    fun provideGetCurrentLocationInfoUseCase(): GetCurrentLocationInfoUseCase =
        GetCurrentLocationInfoUseCase(locationRepository)

    fun provideGetLastLocationInfoUseCase(): GetLastLocationInfoUseCase =
        GetLastLocationInfoUseCase(locationRepository)

    fun provideGetAddressStringByLatLngUseCase(): GetAddressStringByLatLngUseCase =
        GetAddressStringByLatLngUseCase(locationRepository)

    fun provideFetchCurrentWeatherInfoByLocationInfoUseCase()
            : FetchCurrentWeatherInfoByLocationInfoUseCase =
        FetchCurrentWeatherInfoByLocationInfoUseCase(weatherRepository)
}
