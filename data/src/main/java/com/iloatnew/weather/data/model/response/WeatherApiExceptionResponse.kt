package com.iloatnew.weather.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherApiExceptionResponse(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "reason")
    val reason: String
)
