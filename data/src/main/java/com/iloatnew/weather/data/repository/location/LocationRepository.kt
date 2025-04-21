package com.iloatnew.weather.data.repository.location

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    suspend fun getCurrentLocation(): Location

    suspend fun getLastLocation(): Location

    suspend fun getAddressByLatLng(latLng: LatLng): Address
}
