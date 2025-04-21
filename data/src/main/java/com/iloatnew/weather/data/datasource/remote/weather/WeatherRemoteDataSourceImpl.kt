package com.iloatnew.weather.data.datasource.remote.weather

import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRemoteDataSource {
    override suspend fun fetchCurrentWeatherInfoResponseByLatLng(
        latLng: LatLng
    ): CurrentWeatherInfoResponse =
        weatherApi.fetchCurrentWeatherInfoResponseByLatLng(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
        )
}
