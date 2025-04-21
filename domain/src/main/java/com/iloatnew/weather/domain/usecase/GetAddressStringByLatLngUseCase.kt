package com.iloatnew.weather.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.data.model.exception.LocationException
import com.iloatnew.weather.data.repository.location.LocationRepository
import com.iloatnew.weather.domain.extension.toAddressString
import com.iloatnew.weather.domain.extension.toErrorInfo
import com.iloatnew.weather.domain.model.ErrorInfo
import timber.log.Timber

class GetAddressStringByLatLngUseCase(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(latLng: LatLng): Either<ErrorInfo, String> =
        try {
            locationRepository.getAddressByLatLng(latLng).toAddressString().right()
        } catch (exception: LocationException.NullAddressException) {
            exception.toErrorInfo().left()
        } catch (exception: Exception) {
            Timber.e(exception)
            exception.toErrorInfo().left()
        }
}
