package com.iloatnew.weather.domain.model

import java.time.ZonedDateTime

data class WeatherInfo(
    val locationInfo: LocationInfo,
    val elevation: Double,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val time: ZonedDateTime,
    val description: String,
)
