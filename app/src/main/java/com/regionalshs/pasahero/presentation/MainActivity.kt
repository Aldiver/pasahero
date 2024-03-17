package com.regionalshs.pasahero.presentation

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.regionalshs.pasahero.ui.theme.PasaheroTheme
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.regionalshs.pasahero.presentation.components.MapScreen
import com.regionalshs.pasahero.utils.hasLocationPermission
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.rememberCameraPositionState
import com.regionalshs.pasahero.presentation.components.DisplayCard
import com.regionalshs.pasahero.presentation.components.FareMatrixDialog
import com.regionalshs.pasahero.presentation.components.GradientButton
import com.regionalshs.pasahero.presentation.components.ShowSummaryDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationViewModel: LiveTrackingViewModel by viewModels()

        setContent {
            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            var fareMatrix by remember { mutableStateOf(true) }
            val viewState by locationViewModel.viewState.collectAsStateWithLifecycle()
            val trackedLocations by locationViewModel.trackedLocations.collectAsStateWithLifecycle()
            val isLiveTrackingEnabledState by locationViewModel.isLiveTrackingEnabled.collectAsState()
            val distance by locationViewModel.distanceCovered.collectAsState()
            val fare by locationViewModel.fare.collectAsState()
            val elapsedTime by locationViewModel.duration.collectAsState()
            val displaySummary by locationViewModel.isJourneyEnded.collectAsState()

            PasaheroTheme {
                LaunchedEffect(!hasLocationPermission()) {
                    permissionState.launchMultiplePermissionRequest()
                }

                when {
                    permissionState.allPermissionsGranted -> {
                        LaunchedEffect(Unit) {
                            locationViewModel.handle(PermissionEvent.Granted)
                        }
                    }

                    permissionState.shouldShowRationale -> {
                        RationaleAlert(onDismiss = { }) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }

                    !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                        LaunchedEffect(Unit) {
                            locationViewModel.handle(PermissionEvent.Revoked)
                        }
                    }
                }

                with(viewState) {
                    when (this) {
                        ViewState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        ViewState.RevokedPermissions -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("We need permissions to use this app")
                                Button(
                                    onClick = {
                                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    },
                                    enabled = !hasLocationPermission()
                                ) {
                                    if (hasLocationPermission()) CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        color = Color.White
                                    )
                                    else Text("Settings")
                                }
                            }
                        }

                        is ViewState.Success -> {
                            val currentLoc =
                                LatLng(
                                    this.location?.latitude ?: 0.0,
                                    this.location?.longitude ?: 0.0
                                )
                            val cameraState = rememberCameraPositionState()

                            LaunchedEffect(key1 = currentLoc) {
                                cameraState.centerOnLocation(currentLoc)
                            }
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    MapScreen(
                                        currentPosition = LatLng(
                                            currentLoc.latitude,
                                            currentLoc.longitude
                                        ),
                                        cameraState = cameraState,
                                        locations = trackedLocations
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(24.dp),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if(isLiveTrackingEnabledState){
                                            DisplayCard(
                                                distance = distance.toFloat(),
                                                fare = fare.toFloat(),
                                                elapsedTime = elapsedTime,
                                                gradientColors = listOf(
                                                    Color(0xFFC6FFDD), // Hex value: #C6FFDD
                                                    Color(0xFFFBD786), // Hex value: #FBD786
                                                    Color(0xFFf7797d)  // Hex value: #f7797d
                                                )
                                            )
                                        }else{
                                            Spacer(modifier = Modifier.size(100.dp))
                                        }

                                        GradientButton(
                                            text = if (isLiveTrackingEnabledState) "END" else "BEGIN",
                                            textColor = Color.Black,
                                            gradient = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFC6FFDD), // Hex value: #C6FFDD
                                                    Color(0xFFFBD786), // Hex value: #FBD786
                                                    Color(0xFFf7797d)  // Hex value: #f7797d
                                                )
                                            ),
                                            onClick = { locationViewModel.toggleLiveTracking() },
                                        )
                                    }

                                    if(displaySummary){
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.6f))
                                                .clickable { /* Do nothing on click */ },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ShowSummaryDialog(
                                                distance = distance.toFloat(),
                                                fare = fare.toFloat(),
                                                elapsedTime = elapsedTime,
                                                onClose = { locationViewModel.clearTrackedLocations() }
                                            )
                                        }
                                    }
                                }
                        }
                    }
                }

                if(fareMatrix){
                    FareMatrixDialog(
                        onClose = {
                            fareMatrix = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        18f
    ),
    durationMs = 1500
)