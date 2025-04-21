package com.iloatnew.weather.data.model.intercepter

import com.iloatnew.weather.data.model.exception.WeatherApiException
import com.iloatnew.weather.data.model.response.WeatherApiExceptionResponse
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class WeatherApiExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful.not()) {
            val errorBody = response.body?.string()
            if (errorBody != null && errorBody.isNotEmpty()) {
                try {
                    val jsonAdapter =
                        Moshi.Builder().build().adapter(WeatherApiExceptionResponse::class.java)
                    val errorResponse = jsonAdapter.fromJson(errorBody)
                    throw WeatherApiException(
                        message = errorResponse?.reason ?: "",
                        statusCode = response.code
                    )
                } catch (exception: Exception) {
                    Timber.e(exception)
                    throw WeatherApiException(statusCode = response.code)
                }
            } else {
                throw WeatherApiException(statusCode = response.code)
            }
        }

        return response
    }
}
