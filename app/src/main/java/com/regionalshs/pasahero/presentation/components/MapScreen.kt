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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapUiSettings
import com.regionalshs.pasahero.R

@Composable
fun MapScreen(
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    locations: List<LatLng>
) {
//    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    val markerPosition = locations.lastOrNull() ?: currentPosition

    val context = LocalContext.current

    val mapProperties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_dark),
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isMyLocationEnabled = true,
            isTrafficEnabled = false,
            minZoomPreference = 3f,
//            maxZoomPreference = 21f
        )
        )
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = true,
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = false,
            )
        )

    }
    GoogleMap(
        cameraPositionState = cameraState,
        properties = mapProperties,
        uiSettings = mapUiSettings
    ) {
        Polyline(
            points = locations,
            color = Color.Blue,
            width = 5f
        )
        // Marker for current position
//        currentPosition?.let { position ->
//            Marker(
//                state = MarkerState(position = position),
//                title = "Current Position",
//                snippet = "This is your current location",
//                draggable = false
//            )
//        }

        Marker(
            state = MarkerState(position = markerPosition),
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
            title = "Current Position",
            snippet = "This is your current location",
            draggable = false
        )
    }
}