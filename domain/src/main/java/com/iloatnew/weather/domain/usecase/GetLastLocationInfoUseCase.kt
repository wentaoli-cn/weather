package com.iloatnew.weather.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.iloatnew.weather.data.model.exception.LocationException
import com.iloatnew.weather.data.repository.location.LocationRepository
import com.iloatnew.weather.domain.extension.toErrorInfo
import com.iloatnew.weather.domain.extension.toLocationInfo
import com.iloatnew.weather.domain.model.ErrorInfo
import com.iloatnew.weather.domain.model.LocationInfo
import timber.log.Timber

class GetLastLocationInfoUseCase(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(): Either<ErrorInfo, LocationInfo> =
        try {
            locationRepository.getLastLocation().toLocationInfo().right()
        } catch (exception: LocationException.NullLocationException) {
            exception.toErrorInfo().left()
        } catch (exception: LocationException.LocationCanceledException) {
            exception.toErrorInfo().left()
        } catch (exception: Exception) {
            Timber.e(exception)
            exception.toErrorInfo().left()
        }
}
