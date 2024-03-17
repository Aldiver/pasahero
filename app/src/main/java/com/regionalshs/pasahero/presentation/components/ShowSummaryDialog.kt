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

@Composable
fun ShowSummaryDialog(
    distance: Float,
    fare: Float,
    elapsedTime: Long,
    onClose: () -> Unit
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
            Text("Duration - $elapsedTime seconds", fontSize = 20.sp)
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