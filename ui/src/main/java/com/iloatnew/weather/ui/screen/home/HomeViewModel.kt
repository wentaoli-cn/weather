package com.iloatnew.weather.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iloatnew.weather.domain.model.ErrorInfo
import com.iloatnew.weather.domain.model.LocationInfo
import com.iloatnew.weather.domain.model.WeatherInfo
import com.iloatnew.weather.domain.usecase.UseCaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZonedDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCaseProvider: UseCaseProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            uiEvent
                .onEach { toggleIsLoading(true) }
                .flatMapLatest { event ->
                    when (event) {
                        HomeUiEvent.Locate -> handleLocateEvent()
                        HomeUiEvent.Refresh -> handleRefreshEvent()
                        is HomeUiEvent.Add -> handleAddEvent(event.latLng)
                    }.onCompletion { toggleIsLoading(false) }
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    private fun handleLocateEvent(): Flow<Unit> = flow {
        useCaseProvider
            .provideGetCurrentLocationInfoUseCase()
            .invoke()
            .onRight { updateWeatherInfoByLocationInfo(it) }
            .onLeft {
                useCaseProvider
                    .provideGetLastLocationInfoUseCase()
                    .invoke()
                    .onRight { updateWeatherInfoByLocationInfo(it) }
                    .onLeft { addError(it) }
            }
        emit(Unit)
    }

    private fun handleRefreshEvent(): Flow<Unit> = flow {
        uiState.value.weatherInfoList.forEachIndexed { index, weatherInfo ->
            updateWeatherInfoByLocationInfo(
                locationInfo = weatherInfo.locationInfo,
                index = index,
            )
        }
        emit(Unit)
    }

    private fun handleAddEvent(latLng: LatLng): Flow<Unit> = flow {
        updateWeatherInfoByLocationInfo(
            locationInfo = LocationInfo(
                latLng = latLng,
                accuracy = 0.0F,
                address = "",
            ),
            index = uiState.value.weatherInfoList.size
        )
        emit(Unit)
    }

    private fun toggleIsLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    suspend fun triggerEvent(event: HomeUiEvent) {
        Timber.d("triggerEvent: %s", event.toString())
        _uiEvent.emit(event)
    }

    fun toggleFloatingButtonVisibility(isVisible: Boolean) {
        _uiState.update {
            it.copy(shouldShowFloatingButtons = isVisible)
        }
    }

    private fun addError(error: ErrorInfo) {
        _uiState.update {
            it.copy(errorList = it.errorList + listOf(error))
        }
    }

    fun emptyErrorList() {
        _uiState.update {
            it.copy(errorList = emptyList())
        }
    }

    private suspend fun updateWeatherInfoByLocationInfo(
        locationInfo: LocationInfo,
        index: Int = 0,
    ) {
        val core = { weatherInfo: WeatherInfo, index: Int ->
            _uiState.update { current ->
                val updatedList = current.weatherInfoList
                    .toMutableList()
                    .apply {
                        if (index in indices) {
                            this[index] = weatherInfo
                        } else {
                            add(weatherInfo)
                        }
                    }
                    .toList()
                current.copy(weatherInfoList = updatedList)
            }
        }
        useCaseProvider
            .provideFetchCurrentWeatherInfoByLocationInfoUseCase()
            .invoke(locationInfo)
            .onRight { weatherInfo ->
                useCaseProvider
                    .provideGetAddressStringByLatLngUseCase()
                    .invoke(locationInfo.latLng)
                    .onRight { address ->
                        core.invoke(
                            weatherInfo.copy(locationInfo = locationInfo.copy(address = address)),
                            index,
                        )
                    }
                    .onLeft {
                        core.invoke(
                            weatherInfo.copy(locationInfo = locationInfo.copy(address = "")),
                            index,
                        )
                        addError(it)
                    }
            }
            .onLeft {
                core.invoke(
                    WeatherInfo(
                        locationInfo = locationInfo,
                        elevation = 0.0,
                        temperatureCelsius = 0.0,
                        apparentTemperatureCelsius = 0.0,
                        time = ZonedDateTime.now(),
                        description = "",
                    ),
                    index,
                )
                addError(it)
            }
    }
}

data class HomeUiState(
    val errorList: List<ErrorInfo> = emptyList(),
    val isLoading: Boolean = false,
    val shouldShowFloatingButtons: Boolean = false,
    val weatherInfoList: List<WeatherInfo> = emptyList(),
)

sealed class HomeUiEvent {
    object Locate : HomeUiEvent()
    object Refresh : HomeUiEvent()
    data class Add(val latLng: LatLng) : HomeUiEvent()

    override fun toString(): String = when (this) {
        is Locate -> "Locate"
        is Refresh -> "Refresh"
        is Add -> "Add(latLng=$latLng)"
    }
}
