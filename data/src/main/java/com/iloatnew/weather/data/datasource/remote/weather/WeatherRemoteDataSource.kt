package com.iloatnew.weather.data.datasource.remote.weather

import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse

interface WeatherRemoteDataSource {
    suspend fun fetchCurrentWeatherInfoResponseByLatLng(latLng: LatLng): CurrentWeatherInfoResponse
}
