package com.regionalshs.pasahero.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel(context: Context) : ViewModel() {

    // FusedLocationProviderClient to retrieve the current location
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // MutableStateFlow to represent the tracking state
    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    // MutableStateFlow to represent the current location
    private val _currentLocation = MutableStateFlow(LatLng(0.0, 0.0)) // Initial location
    val currentLocation: StateFlow<LatLng> = _currentLocation

    // MutableStateFlow to represent the polylines to draw on the map
    private val _polylines = MutableStateFlow(emptyList<Polyline>())
    val polylines: StateFlow<List<Polyline>> = _polylines

    // Function to toggle tracking state
    fun toggleTracking() {
        _isTracking.value = !_isTracking.value
        if (_isTracking.value) {
            startLocationUpdates()
        } else {
            stopLocationUpdates()
        }
    }

    // Function to start location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(location.latitude, location.longitude)
                    _currentLocation.value = latLng
                    // Start tracking
                    // You may also want to add logic here to draw polylines, calculate distance, fare, etc.
                }
            }
    }

    // Function to stop location updates
    private fun stopLocationUpdates() {
        // Stop tracking
    }
}
