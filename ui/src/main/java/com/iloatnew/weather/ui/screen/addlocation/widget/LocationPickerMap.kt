package com.iloatnew.weather.ui.screen.addlocation.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.DefaultMapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.google.maps.android.compose.widgets.ScaleBar
import com.iloatnew.weather.R
import com.iloatnew.weather.ui.theme.WeatherColor
import com.iloatnew.weather.ui.util.ClickUtil
import com.iloatnew.weather.ui.util.ToastUtil

@Composable
fun LocationPickerMap(
    initialLatLng: LatLng,
    onMapLoaded: () -> Unit,
    onMarkerClicked: (LatLng) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 13F)
    }
    val markerState = rememberUpdatedMarkerState(initialLatLng)
    val markerAnimatable = remember { Animatable(initialLatLng, LatLngConverter) }
    val context = LocalContext.current

    LaunchedEffect(cameraPositionState.position.target) {
        markerAnimatable.animateTo(
            targetValue = cameraPositionState.position.target,
            animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = DefaultMapUiSettings.copy(mapToolbarEnabled = false),
            onMapLoaded = {
                ToastUtil.showShort(context, R.string.add_location_marker_toast)
                onMapLoaded.invoke()
            },
        ) {
            MarkerComposable(
                state = markerState.apply {
                    position = markerAnimatable.value
                },
                onClick = { marker ->
                    ClickUtil.throttle {
                        onMarkerClicked.invoke(marker.position)
                    }.invoke()
                    true
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_marker_48),
                    contentDescription = stringResource(R.string.add_location_marker_description),
                    tint = WeatherColor.crimson,
                )
            }
            Circle(
                center = initialLatLng,
                radius = DISTANCE_THRESHOLD,
                fillColor = Color.Black.copy(alpha = 0.35F),
                strokeWidth = 2F,
            )
        }

        ScaleBar(
            modifier = Modifier
                .padding(top = 12.dp, end = 12.dp)
                .align(Alignment.TopEnd),
            cameraPositionState = cameraPositionState,
        )
    }
}

private val LatLngConverter = TwoWayConverter<LatLng, AnimationVector2D>(
    convertToVector = { latLng ->
        AnimationVector2D(latLng.latitude.toFloat(), latLng.longitude.toFloat())
    },
    convertFromVector = { vector ->
        LatLng(vector.v1.toDouble(), vector.v2.toDouble())
    },
)

const val DISTANCE_THRESHOLD = 3_000.0
