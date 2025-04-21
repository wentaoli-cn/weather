package com.iloatnew.weather.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherInfoResponse(
    @Json(name = "current")
    val current: Current,
    @Json(name = "current_units")
    val currentUnits: CurrentUnits,
    @Json(name = "elevation")
    val elevation: Double,
    @Json(name = "generationtime_ms")
    val generationtimeMs: Double,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "timezone")
    val timezone: String,
    @Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,
    @Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Int
) {
    @JsonClass(generateAdapter = true)
    data class Current(
        @Json(name = "apparent_temperature")
        val apparentTemperature: Double,
        @Json(name = "interval")
        val interval: Int,
        @Json(name = "temperature_2m")
        val temperature2m: Double,
        @Json(name = "time")
        val time: String,
        @Json(name = "weather_code")
        val weatherCode: Int
    )

    @JsonClass(generateAdapter = true)
    data class CurrentUnits(
        @Json(name = "apparent_temperature")
        val apparentTemperature: String,
        @Json(name = "interval")
        val interval: String,
        @Json(name = "temperature_2m")
        val temperature2m: String,
        @Json(name = "time")
        val time: String,
        @Json(name = "weather_code")
        val weatherCode: String
    )
}
