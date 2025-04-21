package com.iloatnew.weather.ui.screen.addlocation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.R
import com.iloatnew.weather.ui.screen.addlocation.widget.LocationPickerMap
import com.iloatnew.weather.ui.widget.WeatherProgressIndicator
import com.iloatnew.weather.ui.widget.WeatherTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    initialLatLng: LatLng,
    navigateBackWithLatLng: (LatLng?) -> Unit,
    viewModel: AddLocationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WeatherTopBar(
                title = stringResource(R.string.add_location_title),
                onNavigationIconClicked = { navigateBackWithLatLng.invoke(null) },
            )
        },
    ) { paddingValues ->
        WeatherProgressIndicator(uiState.isLoading)

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            LocationPickerMap(
                initialLatLng = initialLatLng,
                onMapLoaded = { viewModel.toggleIsLoading(false) },
                onMarkerClicked = navigateBackWithLatLng,
            )
        }
    }
}
