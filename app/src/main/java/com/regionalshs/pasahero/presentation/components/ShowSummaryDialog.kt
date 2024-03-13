package com.regionalshs.pasahero.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowSummaryDialog(
    distance: Float,
    fare: Float,
    elapsedTime: Long,
    onClose: () -> Unit // Callback to close the dialog
) {
    // Assume dialog implementation here
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
//        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Summary: Distance - $distance km", fontSize = 20.sp)
            Text("Fare - ₹$fare", fontSize = 20.sp)
            Text("Elapsed Time - $elapsedTime seconds", fontSize = 20.sp)

            Button(
                onClick = onClose, // Close the dialog
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Close")
            }
        }
    }
}