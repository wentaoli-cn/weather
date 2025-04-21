package com.iloatnew.weather.data.repository.weather

import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse

interface WeatherRepository {
    suspend fun fetchCurrentWeatherInfoResponseByLatLng(
        latLng: LatLng
    ): CurrentWeatherInfoResponse
}
