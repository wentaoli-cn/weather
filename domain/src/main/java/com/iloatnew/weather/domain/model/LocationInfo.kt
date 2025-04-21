package com.iloatnew.weather.domain.model

import com.google.android.gms.maps.model.LatLng

data class LocationInfo(
    val latLng: LatLng,
    val accuracy: Float,
    val address: String,
)
