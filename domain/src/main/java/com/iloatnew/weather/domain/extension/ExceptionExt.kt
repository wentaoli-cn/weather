package com.iloatnew.weather.domain.extension

import com.iloatnew.weather.data.model.exception.LocationException
import com.iloatnew.weather.data.model.exception.WeatherApiException
import com.iloatnew.weather.domain.model.ButtonAction
import com.iloatnew.weather.domain.model.COMMON_ERROR_MESSAGE_2
import com.iloatnew.weather.domain.model.COMMON_ERROR_MESSAGE_3
import com.iloatnew.weather.domain.model.ErrorInfo
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun LocationException.NullLocationException.toErrorInfo(): ErrorInfo =
    ErrorInfo(message = "Failed to locate your position.")

fun LocationException.NullAddressException.toErrorInfo(): ErrorInfo =
    ErrorInfo(message = "Failed to retrieve your address.")

fun LocationException.LocationCanceledException.toErrorInfo(): ErrorInfo =
    ErrorInfo(message = "Location request has been canceled.")

fun Exception.toErrorInfo(): ErrorInfo =
    if (isNetworkException()) {
        ErrorInfo(
            message = COMMON_ERROR_MESSAGE_2,
            positiveButtonAction = ButtonAction.Setting,
            negativeButtonAction = ButtonAction.Cancel,
        )
    } else if (this is WeatherApiException) {
        ErrorInfo(
            message = message?.takeIf { it.isNotEmpty() } ?: COMMON_ERROR_MESSAGE_3,
            statusCode = statusCode,
            positiveButtonAction = ButtonAction.Retry,
            negativeButtonAction = ButtonAction.Cancel,
        )
    } else {
        ErrorInfo()
    }

private fun Exception.isNetworkException(): Boolean =
    this is UnknownHostException || this is SocketTimeoutException || this is SocketException
