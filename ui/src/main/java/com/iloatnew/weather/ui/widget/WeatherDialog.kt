package com.iloatnew.weather.ui.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.iloatnew.weather.ui.util.ClickUtil

@Composable
fun SingleButtonDialog(
    buttonText: String,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = ClickUtil.throttle { onDismissRequest.invoke() },
            confirmButton = {
                TextButton(
                    onClick = ClickUtil.throttle {
                        openDialog.value = false
                        onClick.invoke()
                    },
                ) {
                    Text(buttonText)
                }
            },
            modifier = modifier,
            title = title?.let { { Text(text = it) } },
            text = message?.let { { Text(text = it) } },
            shape = shape,
            containerColor = containerColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            tonalElevation = tonalElevation,
            properties = properties,
        )
    }
}

@Composable
fun DoubleButtonDialog(
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = ClickUtil.throttle { onDismissRequest.invoke() },
            confirmButton = {
                TextButton(
                    onClick = ClickUtil.throttle {
                        openDialog.value = false
                        onPositiveClicked.invoke()
                    },
                ) {
                    Text(positiveButtonText)
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(
                    onClick = ClickUtil.throttle {
                        openDialog.value = false
                        onNegativeClicked.invoke()
                    },
                ) {
                    Text(negativeButtonText)
                }
            },
            title = title?.let { { Text(text = it) } },
            text = message?.let { { Text(text = it) } },
            shape = shape,
            containerColor = containerColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            tonalElevation = tonalElevation,
            properties = properties,
        )
    }
}
