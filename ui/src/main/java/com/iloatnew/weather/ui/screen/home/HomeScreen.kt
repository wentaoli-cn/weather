package com.iloatnew.weather.ui.screen.home

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.R
import com.iloatnew.weather.domain.model.ButtonAction
import com.iloatnew.weather.ui.screen.addlocation.widget.DISTANCE_THRESHOLD
import com.iloatnew.weather.ui.screen.home.widget.FloatingAddButton
import com.iloatnew.weather.ui.screen.home.widget.FloatingLocationButton
import com.iloatnew.weather.ui.screen.home.widget.FloatingRefreshButton
import com.iloatnew.weather.ui.screen.home.widget.LocationPermissionRequester
import com.iloatnew.weather.ui.screen.home.widget.WeatherInfoPager
import com.iloatnew.weather.ui.util.ClickUtil
import com.iloatnew.weather.ui.util.LocationUtil
import com.iloatnew.weather.ui.util.ToastUtil
import com.iloatnew.weather.ui.widget.WeatherErrorHandler
import com.iloatnew.weather.ui.widget.WeatherProgressIndicator
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    resultLatLng: LatLng?,
    navigateToAddLocation: (LatLng) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val actionListener = { action: ButtonAction ->
        when (action) {
            ButtonAction.Retry -> {
                coroutineScope.launch {
                    viewModel.triggerEvent(event = HomeUiEvent.Refresh)
                }
            }

            ButtonAction.Setting -> {
                context.startActivity(
                    Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            else -> {}
        }
        viewModel.emptyErrorList()
    }

    LaunchedEffect(Unit) {
        resultLatLng?.let {
            val shouldAdd = uiState.weatherInfoList.all { item ->
                LocationUtil.distanceBetween(it, item.locationInfo.latLng) >= DISTANCE_THRESHOLD
            }
            if (shouldAdd) {
                viewModel.triggerEvent(HomeUiEvent.Add(it))
            } else {
                ToastUtil.showShort(context, R.string.home_floating_add_button_toast_2)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (uiState.shouldShowFloatingButtons) {
                Column {
                    FloatingLocationButton(
                        onClick = ClickUtil.throttle(scope = coroutineScope) {
                            viewModel.triggerEvent(
                                HomeUiEvent.Locate
                            )
                        },
                    )
                    FloatingAddButton(
                        onClick = ClickUtil.throttle {
                            if (uiState.weatherInfoList.isNotEmpty()) {
                                navigateToAddLocation.invoke(
                                    uiState.weatherInfoList.first().locationInfo.latLng
                                )
                            } else {
                                ToastUtil.showShort(
                                    context,
                                    R.string.home_floating_add_button_toast_1,
                                )
                            }
                        },
                    )
                    FloatingRefreshButton(
                        onClick = ClickUtil.throttle(scope = coroutineScope) {
                            viewModel.triggerEvent(
                                HomeUiEvent.Refresh
                            )
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        WeatherProgressIndicator(uiState.isLoading)

        WeatherErrorHandler(
            error = uiState.errorList.firstOrNull(),
            onPositiveClicked = actionListener,
            onNegativeClicked = actionListener,
        )

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            LocationPermissionRequester(
                onPermissionsResult = { isGranted ->
                    ClickUtil.throttle(scope = coroutineScope) {
                        if (isGranted) {
                            viewModel.toggleFloatingButtonVisibility(true)
                            viewModel.triggerEvent(HomeUiEvent.Locate)
                        }
                    }.invoke()
                }
            )

            WeatherInfoPager(uiState.weatherInfoList)
        }
    }
}
