package com.iloatnew.weather.domain.extension

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.domain.model.LocationInfo

fun Location.toLocationInfo(): LocationInfo =
    LocationInfo(
        latLng = LatLng(latitude, longitude),
        accuracy = accuracy,
        address = "",
    )

fun Address.toAddressString(): String =
    listOf(
        countryName,
        locality,
        subLocality,
        thoroughfare,
    ).filterNot { it.isNullOrEmpty() }.joinToString()
