package com.regionalshs.pasahero.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.regionalshs.pasahero.domain.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class LiveTrackingViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    var isLiveTrackingEnabled = false

    private val _trackedLocations = MutableStateFlow<List<LatLng>>(emptyList())
    val trackedLocations: StateFlow<List<LatLng>> = _trackedLocations
    init {
        // Initialize location on startup
        getLocation()
    }

    private fun getLocation() {
        viewModelScope.launch {
            getLocationUseCase.invoke().collect { location ->
                val updatedList = _trackedLocations.value.toMutableList()
                location?.let { updatedList.add(it) }
                _trackedLocations.value = updatedList
            }
        }
    }

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
        isLiveTrackingEnabled = !isLiveTrackingEnabled
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