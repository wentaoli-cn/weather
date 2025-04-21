package com.iloatnew.weather.data.datasource.remote.weather

import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast?current=temperature_2m,weather_code,apparent_temperature")
    suspend fun fetchCurrentWeatherInfoResponseByLatLng(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): CurrentWeatherInfoResponse
}
