package com.regionalshs.pasahero.utils

fun formatDuration(elapsedTime: Long): String {
    val hours = elapsedTime / 3600
    val minutes = (elapsedTime % 3600) / 60
    val seconds = elapsedTime % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}