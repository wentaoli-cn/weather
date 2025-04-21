package com.iloatnew.weather.data.datasource.local.location

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng

interface LocationLocalDataSource {
    suspend fun getCurrentLocation(): Location

    suspend fun getLastLocation(): Location

    suspend fun getAddressByLatLng(latLng: LatLng): Address
}
