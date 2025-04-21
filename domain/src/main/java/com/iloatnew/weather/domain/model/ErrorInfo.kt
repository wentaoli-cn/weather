package com.iloatnew.weather.domain.model

data class ErrorInfo(
    val title: String = COMMON_ERROR_TITLE,
    val message: String = COMMON_ERROR_MESSAGE_1,
    val statusCode: Int = 0,
    val positiveButtonAction: ButtonAction = ButtonAction.Okay,
    val negativeButtonAction: ButtonAction = ButtonAction.None,
    val cancelable: Boolean = false,
)

enum class ButtonAction {
    Retry,
    Cancel,
    Okay,
    Setting,
    None,
}

const val COMMON_ERROR_TITLE = "Error"
const val COMMON_ERROR_MESSAGE_1 = "Oops！Something went wrong."
const val COMMON_ERROR_MESSAGE_2 = "Please check if the network connection is working properly."
const val COMMON_ERROR_MESSAGE_3 = "Network error. Please try again!"
