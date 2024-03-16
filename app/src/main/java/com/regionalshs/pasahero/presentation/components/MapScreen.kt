package com.regionalshs.pasahero.presentation.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.regionalshs.pasahero.presentation.LiveTrackingViewModel
import java.lang.reflect.Modifier
import androidx.activity.viewModels

@Composable
fun MapScreen(
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    locations: List<LatLng>
) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.HYBRID,
            isTrafficEnabled = true
        )
    ) {
        Polyline(
            points = locations,
            color = Color.Blue,
            width = 5f
        )
        // Marker for current position
        currentPosition?.let { position ->
            Marker(
                state = MarkerState(position = position),
                title = "Current Position",
                snippet = "This is your current location",
                draggable = false
            )
        }
    }
}