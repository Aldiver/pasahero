package com.regionalshs.pasahero.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.maps.android.compose.GoogleMap
import com.regionalshs.pasahero.presentation.components.DisplayCard
import com.regionalshs.pasahero.presentation.components.GoogleMapLiveRoute
import com.regionalshs.pasahero.presentation.components.ShowSummaryDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LiveTrackingApp() {
    var isTracking by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(0f) }
    var fare by remember { mutableStateOf(0f) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var currentLocation by remember { mutableStateOf("Current Location") }
    var showSummaryDialog by remember { mutableStateOf(false) } // Control whether to show the summary dialog

    Box {
        GoogleMapLiveRoute(
            initialLocation = null,
            polylines = null)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isTracking) {
                DisplayCard(distance, fare, elapsedTime)
            }

            // Show the summary dialog if necessary
            if (showSummaryDialog) {
                ShowSummaryDialog(distance = distance, fare = fare, elapsedTime = elapsedTime) {
                    // Callback to reset the tracking state and hide the dialog
                    isTracking = false
                    showSummaryDialog = false
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    isTracking = !isTracking
                    if (!isTracking) {
                        // Show the summary dialog when tracking is stopped
                        showSummaryDialog = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (isTracking) "Stop" else "Start")
            }
        }
    }
}

