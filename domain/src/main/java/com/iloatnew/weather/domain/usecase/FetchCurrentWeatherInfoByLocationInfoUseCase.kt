package com.iloatnew.weather.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.iloatnew.weather.data.repository.weather.WeatherRepository
import com.iloatnew.weather.domain.extension.toErrorInfo
import com.iloatnew.weather.domain.extension.toWeatherInfo
import com.iloatnew.weather.domain.model.ErrorInfo
import com.iloatnew.weather.domain.model.LocationInfo
import com.iloatnew.weather.domain.model.WeatherInfo

class FetchCurrentWeatherInfoByLocationInfoUseCase(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(locationInfo: LocationInfo): Either<ErrorInfo, WeatherInfo> =
        try {
            weatherRepository
                .fetchCurrentWeatherInfoResponseByLatLng(locationInfo.latLng)
                .toWeatherInfo(locationInfo)
                .right()
        } catch (exception: Exception) {
            exception.toErrorInfo().left()
        }
}
