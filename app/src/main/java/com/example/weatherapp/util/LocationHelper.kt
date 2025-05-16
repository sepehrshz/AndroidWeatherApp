package com.example.weatherapp.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class LocationHelper(private val activity : Activity) {
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation():Location? {
        val locationManager = activity.getSystemService(Activity.LOCATION_SERVICE) as LocationManager

        if(ActivityCompat.checkSelfPermission(activity , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            return null
        }

        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}