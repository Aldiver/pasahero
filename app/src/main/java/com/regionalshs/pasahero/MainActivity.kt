package com.regionalshs.pasahero

import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.regionalshs.pasahero.presentation.LiveTrackingApp
import com.regionalshs.pasahero.ui.theme.PasaheroTheme
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasaheroTheme {

                LiveTrackingApp()
            }
        }
    }
}
