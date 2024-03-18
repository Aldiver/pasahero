package com.regionalshs.pasahero.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.regionalshs.pasahero.utils.formatDuration

@Composable
fun ShowSummaryDialog(
    distance: Float,
    fare: Float,
    elapsedTime: Long,
    onClose: () -> Unit,
    locations: List<LatLng>
) {
    // Assume dialog implementation here
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF242F3E),
            contentColor = Color.White,
        )
//        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Fare : ₱${"%.2f".format(fare)}",
                fontSize = 45.sp,
            )
            Text("Distance: ${"%.2f".format(distance)} km", fontSize = 20.sp)
            Text("Duration: ${formatDuration(elapsedTime)}", fontSize = 20.sp)
            Text("Initial Lat: ${locations.firstOrNull()?.latitude ?: 0.0}, ${locations.firstOrNull()?.longitude ?: 0.0}", fontSize = 16.sp)
            Text("Last Lat: ${locations.lastOrNull()?.latitude ?: 0.0}, ${locations.lastOrNull()?.longitude ?: 0.0}", fontSize = 16.sp)

            Spacer(modifier = Modifier.size(30.dp))

            Button(
                onClick = { onClose() }, // Close the dialog
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF141C27)
                ),
            ) {
                Text("Close")
            }
        }
    }
}