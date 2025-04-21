package com.iloatnew.weather.data.datasource.local.location

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.iloatnew.weather.data.model.exception.LocationException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationLocalDataSourceImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) : LocationLocalDataSource {
    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override suspend fun getCurrentLocation(): Location = suspendCoroutine {
        fusedLocationProviderClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token,
            )
            .addOnSuccessListener { location ->
                if (location != null) {
                    it.resume(location)
                } else {
                    it.resumeWithException(LocationException.NullLocationException)
                }
            }
            .addOnCanceledListener {
                it.resumeWithException(LocationException.LocationCanceledException)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override suspend fun getLastLocation(): Location = suspendCoroutine {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    it.resume(location)
                } else {
                    it.resumeWithException(LocationException.NullLocationException)
                }
            }
            .addOnCanceledListener {
                it.resumeWithException(LocationException.LocationCanceledException)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    override suspend fun getAddressByLatLng(latLng: LatLng): Address = suspendCoroutine {
        val handleAddress = { address: Address? ->
            if (address != null) {
                it.resume(address)
            } else {
                it.resumeWithException(LocationException.NullAddressException)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                handleAddress(addresses.firstOrNull())
            }
        } else {
            handleAddress(
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.first()
            )
        }
    }
}
