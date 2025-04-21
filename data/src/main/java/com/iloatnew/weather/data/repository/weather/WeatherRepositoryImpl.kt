package com.iloatnew.weather.data.repository.weather

import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.datasource.remote.weather.WeatherRemoteDataSource
import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {
    override suspend fun fetchCurrentWeatherInfoResponseByLatLng(
        latLng: LatLng
    ): CurrentWeatherInfoResponse =
        weatherRemoteDataSource.fetchCurrentWeatherInfoResponseByLatLng(latLng)
}
