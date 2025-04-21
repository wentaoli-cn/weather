package com.iloatnew.weather.data.model.exception

import java.io.IOException

class WeatherApiException(val statusCode: Int, message: String = "") : IOException(message)
