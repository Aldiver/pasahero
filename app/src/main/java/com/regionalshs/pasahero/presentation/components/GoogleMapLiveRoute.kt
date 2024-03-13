package com.regionalshs.pasahero.presentation.components

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.regionalshs.pasahero.R

//Modify GoogleMapLiveRoute to - initialize at current location
//When Start is Click = Start Live Route, put a marker on current location
//Every update of new current location, draw polyline
//display current Distance in Display Card
//display current fare in Display Card where
//display current elapsed time in Display card
//So there is a view Model for it as I presume
//apply dark theme for google map
//setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.style_json));
//mapType is mapType.normal
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapLiveRoute(
    initialLocation: LatLng?,
    // Current location
    polylines: List<Polyline>?,
) {
//    val cameraPositionState = rememberCameraPositionState()

    val context = LocalContext.current
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    var lineType by remember {
        mutableStateOf<Polyline?>(null)
    }

    val latlangList = remember {
        mutableStateListOf(initialLocation)
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 17f)
    }


    val locationPermission: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    LaunchedEffect(key1 = locationPermission.permissions) {
        locationPermission.launchMultiplePermissionRequest()
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_dark),
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isMyLocationEnabled = locationPermission.allPermissionsGranted,
            isTrafficEnabled = false,
            minZoomPreference = 3f,
//            maxZoomPreference = 21f
            )
        )
    }

    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(
            zoomControlsEnabled = false,
            zoomGesturesEnabled = true,
            myLocationButtonEnabled = true
        ))
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings
    ){

    }
}