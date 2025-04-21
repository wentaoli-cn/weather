package com.iloatnew.weather.domain.extension

import com.iloatnew.weather.data.model.response.CurrentWeatherInfoResponse
import com.iloatnew.weather.domain.model.LocationInfo
import com.iloatnew.weather.domain.model.WeatherInfo
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

fun CurrentWeatherInfoResponse.toWeatherInfo(locationInfo: LocationInfo): WeatherInfo =
    WeatherInfo(
        locationInfo = locationInfo,
        elevation = elevation,
        temperatureCelsius = current.temperature2m,
        apparentTemperatureCelsius = current.apparentTemperature,
        time = current.time.toLocalZonedDateTimeFromUtc(),
        description = current.weatherCode.toWeatherDescription(),
    )

private fun Int.toWeatherDescription(): String = when (this) {
    0 -> "Clear sky"
    1 -> "Mainly clear"
    2 -> "Partly cloudy"
    3 -> "Overcast"
    45 -> "Fog"
    48 -> "Depositing rime fog"
    51 -> "Drizzle (Light)"
    53 -> "Drizzle (Moderate)"
    55 -> "Drizzle (Dense)"
    56 -> "Freezing drizzle (Light)"
    57 -> "Freezing drizzle (Dense)"
    61 -> "Rain (Slight)"
    63 -> "Rain (Moderate)"
    65 -> "Rain (Heavy)"
    66 -> "Freezing rain (Light)"
    67 -> "Freezing rain (Heavy)"
    71 -> "Snow fall (Slight)"
    73 -> "Snow fall (Moderate)"
    75 -> "Snow fall (Heavy)"
    77 -> "Snow grains"
    80 -> "Rain showers (Slight)"
    81 -> "Rain showers (Moderate)"
    82 -> "Rain showers (Violent)"
    85 -> "Snow showers (Slight)"
    86 -> "Snow showers (Heavy)"
    95 -> "Thunderstorm"
    96 -> "Thunderstorm with slight hail"
    99 -> "Thunderstorm with heavy hail"
    else -> ""
}

private fun String.toLocalZonedDateTimeFromUtc(): ZonedDateTime {
    return if (this.isEmpty()) {
        ZonedDateTime.now()
    } else {
        try {
            ZonedDateTime.parse(if (this.contains("+00:00")) this else "$this+00:00")
                .withZoneSameInstant(ZoneId.systemDefault())
        } catch (exception: DateTimeParseException) {
            Timber.e(exception)
            ZonedDateTime.now()
        }
    }
}
