package com.iloatnew.weather.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.iloatnew.weather.domain.model.ButtonAction
import com.iloatnew.weather.domain.model.ErrorInfo

@Composable
fun WeatherErrorHandler(
    error: ErrorInfo?,
    onPositiveClicked: (ButtonAction) -> Unit,
    onNegativeClicked: (ButtonAction) -> Unit,
) {
    error?.let {
        if (error.positiveButtonAction != ButtonAction.None && error.negativeButtonAction != ButtonAction.None) {
            DoubleButtonDialog(
                positiveButtonText = error.positiveButtonAction.name,
                negativeButtonText = error.negativeButtonAction.name,
                onPositiveClicked = {
                    onPositiveClicked.invoke(error.positiveButtonAction)
                },
                onNegativeClicked = {
                    onNegativeClicked.invoke(error.negativeButtonAction)
                },
                onDismissRequest = {},
                title = error.title,
                message = error.message,
                properties =
                    if (error.cancelable)
                        DialogProperties()
                    else
                        DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false,
                        ),
            )
        } else {
            SingleButtonDialog(
                buttonText =
                    if (error.positiveButtonAction != ButtonAction.None)
                        error.positiveButtonAction.name
                    else
                        ButtonAction.Okay.name,
                onClick = {
                    onPositiveClicked.invoke(error.positiveButtonAction)
                },
                onDismissRequest = {},
                title = error.title,
                message = error.message,
                properties =
                    if (error.cancelable)
                        DialogProperties()
                    else
                        DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false,
                        ),
            )
        }
    }
}
