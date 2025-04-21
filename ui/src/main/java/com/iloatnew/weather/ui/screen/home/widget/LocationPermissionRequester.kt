package com.iloatnew.weather.ui.screen.home.widget

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.iloatnew.weather.R
import com.iloatnew.weather.ui.theme.WeatherColor
import com.iloatnew.weather.ui.util.ClickUtil

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequester(
    onPermissionsResult: (isGranted: Boolean) -> Unit,
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val isGranted = locationPermissionsState.permissions.first().status == PermissionStatus.Granted
    val context = LocalContext.current

    LaunchedEffect(isGranted) {
        onPermissionsResult(isGranted)
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    if (!locationPermissionsState.allPermissionsGranted) {
        Column {
            val allPermissionsRevoked = locationPermissionsState.permissions.size ==
                    locationPermissionsState.revokedPermissions.size

            Text(
                text = stringResource(
                    if (allPermissionsRevoked)
                        R.string.home_location_permission_handler_tip_1
                    else
                        R.string.home_location_permission_handler_tip_2
                ),
                color =
                    if (allPermissionsRevoked)
                        WeatherColor.crimson
                    else
                        Color.Black,
            )

            Button(
                onClick = ClickUtil.throttle {
                    if (locationPermissionsState.shouldShowRationale) {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    } else {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                }
            ) {
                Text(
                    stringResource(
                        if (allPermissionsRevoked) R.string.home_location_permission_handler_button_1
                        else R.string.home_location_permission_handler_button_2
                    )
                )
            }
        }
    }
}
