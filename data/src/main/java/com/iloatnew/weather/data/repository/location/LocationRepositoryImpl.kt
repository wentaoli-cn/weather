package com.iloatnew.weather.data.repository.location

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.datasource.local.location.LocationLocalDataSource
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationLocalDataSource: LocationLocalDataSource
) : LocationRepository {
    override suspend fun getCurrentLocation(): Location =
        locationLocalDataSource.getCurrentLocation()

    override suspend fun getLastLocation(): Location =
        locationLocalDataSource.getLastLocation()

    override suspend fun getAddressByLatLng(latLng: LatLng): Address =
        locationLocalDataSource.getAddressByLatLng(latLng)
}
