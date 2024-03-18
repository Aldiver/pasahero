package com.regionalshs.pasahero.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

object LocationUtils {
    private const val EARTH_RADIUS = 6371 // Radius of the Earth in kilometers

    /**
     * Calculate the distance between two LatLng points using Haversine formula.
     *
     * @param startLatLng The starting LatLng point.
     * @param endLatLng The ending LatLng point.
     * @return The distance between the two points in kilometers.
     */
    fun calculateDistance(startLatLng: LatLng, endLatLng: LatLng): Double {
        val startLat = Math.toRadians(startLatLng.latitude)
        val startLng = Math.toRadians(startLatLng.longitude)
        val endLat = Math.toRadians(endLatLng.latitude)
        val endLng = Math.toRadians(endLatLng.longitude)

        val dLat = endLat - startLat
        val dLng = endLng - startLng

        val a = sin(dLat / 2).pow(2) + cos(startLat) * cos(endLat) * sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS * c
    }
}
