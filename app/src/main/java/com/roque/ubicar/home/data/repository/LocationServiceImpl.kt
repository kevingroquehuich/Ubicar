package com.roque.ubicar.home.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.roque.ubicar.home.data.mapper.toDomain
import com.roque.ubicar.home.domain.LocationService
import com.roque.ubicar.home.domain.model.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationServiceImpl(
    private val context: Context
) : LocationService {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    override fun getLocationUpdates(distanceToDestination: Float): Flow<Location?> = callbackFlow {
        if (!hasLocationPermission(context)) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val request = buildLocationRequest(distanceToDestination)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let {
                    trySend(it.toDomain())
                }
            }
        }

        locationClient.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())

        awaitClose {
            stopLocationUpdates()
        }
    }

    private fun buildLocationRequest(distanceToDestination: Float): LocationRequest {
        return if (distanceToDestination > 100f) {
            LocationRequest.Builder(30000L)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateDistanceMeters(10f)
                .build()
        } else {
            LocationRequest.Builder(10000L)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setWaitForAccurateLocation(true)
                .setMinUpdateDistanceMeters(5f)
                .build()
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    override fun stopLocationUpdates() {
        locationCallback?.let {
            locationClient.removeLocationUpdates(it)
        }
    }

    override suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission(context)) return null

        val result = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
        return result.toDomain()
    }


}