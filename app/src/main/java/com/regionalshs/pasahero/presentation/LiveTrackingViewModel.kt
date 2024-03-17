package com.regionalshs.pasahero.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.regionalshs.pasahero.domain.GetLocationUseCase
import com.regionalshs.pasahero.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class LiveTrackingViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _isLiveTrackingEnabled = MutableStateFlow(false)
    val isLiveTrackingEnabled: StateFlow<Boolean> = _isLiveTrackingEnabled
    private val _isJourneyEnded = MutableStateFlow(false)
    val isJourneyEnded: StateFlow<Boolean> = _isJourneyEnded
    private var locationJob: Job? = null
    private var durationJob: Job? = null

    private val _trackedLocations = MutableStateFlow<List<LatLng>>(emptyList())
    val trackedLocations: StateFlow<List<LatLng>> = _trackedLocations

    // Additional observables for distance, fare, and duration
    private val _distanceCovered = MutableStateFlow(0.0)
    val distanceCovered: StateFlow<Double> = _distanceCovered

    private val _fare = MutableStateFlow(0.0)
    val fare: StateFlow<Double> = _fare

    private var locationInitiated = false
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val durationUpdateFlow = flow {
        while (true) {
            emit(Unit) // Emit a unit every second
            delay(1000) // Delay for one second
        }
    }

    private var lastLocation: LatLng? = null

    fun handle(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke().collect {
                        _viewState.value = ViewState.Success(it)
                    }
                }
            }

            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    fun toggleLiveTracking() {
        _isLiveTrackingEnabled.value = !_isLiveTrackingEnabled.value
        if (_isLiveTrackingEnabled.value) {
            startTrackingLocation()
        } else {
            stopTrackingLocation()
        }
    }

    fun clearTrackedLocations() {
        _trackedLocations.value = emptyList()
        _isJourneyEnded.value = false
        _duration.value = 0L
        _distanceCovered.value = 0.0
        _fare.value = 0.0
    }

    private fun startTrackingLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            getLocationUseCase.invoke().collect { location ->
                lastLocation?.let { last ->
                    if (!locationInitiated && location != null) {
                        locationInitiated = true // Set locationInitiated to true if location is found
                        startDurationTracking() // Start duration tracking coroutine
                    }
                    val distance = location?.let { LocationUtils.calculateDistance(last, it) }
                    if (distance != null) {
                        _distanceCovered.value += distance
                    }
                    _fare.value = calculateFare(_distanceCovered.value)
//                    _duration.value += 1 // Increment duration by 1 second
                }
                lastLocation = location
                val updatedList = _trackedLocations.value.toMutableList()
                location?.let { updatedList.add(it) }
                _trackedLocations.value = updatedList
            }
        }
    }

    private fun startDurationTracking() {

        durationJob?.cancel()
        durationJob = viewModelScope.launch {
            durationUpdateFlow.collect {
                _duration.value += 1 // Increment duration by 1 second every time the flow emits a value
            }
        }
    }

    private fun stopTrackingLocation() {
        _isJourneyEnded.value = true
        locationJob?.cancel()
        durationJob?.cancel()
        locationInitiated = false
    }

    private fun calculateFare(distanceCovered: Double): Double {
        // Fare calculation logic
        val baseFare = 20.0 // Fare for the first kilometer
        val additionalFarePerKm = 5.0 // Additional fare for every succeeding kilometer

        return if (distanceCovered <= 1.0) {
            baseFare // If distance covered is less than or equal to 1 km, return the base fare
        } else {
            val additionalDistance = distanceCovered - 1.0 // Exclude the first kilometer
            val additionalKm = additionalDistance.toInt() // Round down to the nearest kilometer

            val fareForAdditionalKm = additionalFarePerKm * additionalKm // Calculate fare for additional kilometers

            baseFare + fareForAdditionalKm // Total fare
        }
    }
}

sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}