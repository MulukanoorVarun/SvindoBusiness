package com.svindo.vendorapp.activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.svindo.vendorapp.R
import com.svindo.vendorapp.R.*
import java.io.IOException
import java.util.*


@SuppressLint("Registered")
class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var currentmarker: Marker? = null
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation: Location?=null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_google_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    private fun fetchLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }

       val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if(location != null) {
                this.currentLocation=location
                Log.d("curlocation",currentLocation.toString())
                val mapFragment = supportFragmentManager.findFragmentById(id.mapFragment) as SupportMapFragment
                 mapFragment.getMapAsync(this)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("MapDebug", "onMapReady called")
        // Add a marker at an initial location
            val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
            drawMarker(latlong)
        // Set up a marker drag listener
       currentmarker?.isDraggable = true
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {
//                val updatedLocation = p0?.position
//                if (updatedLocation != null) {
//                    val latitude = updatedLocation.latitude
//                    val longitude = updatedLocation.longitude
//                    // Do something with the updated location (e.g., display it)
//                    // latitude and longitude now contain the new location
//                }
            }

            override fun onMarkerDragEnd(p0: Marker) {
                if(currentmarker !=null)
                    currentmarker?.remove()
                val newLatLng = LatLng(p0?.position!!.latitude,p0?.position!!.longitude)
                drawMarker(newLatLng)
            }

            override fun onMarkerDragStart(p0: Marker) {

            }
        })
    }

    internal fun drawMarker(latlong:LatLng){
        val marker= MarkerOptions()
            .position(latlong)
            .title("I am here")
            .snippet(getAddress(latlong.latitude,latlong.longitude))
            .draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
        currentmarker=mMap.addMarker(marker)
        currentmarker?.showInfoWindow()
    }


    private fun getAddress(lat:Double,lon:Double): String? {
        val geocoder= Geocoder(this, Locale.getDefault())
        val addresses=geocoder.getFromLocation(lat,lon,1)
        return addresses!![0].getAddressLine(0).toString()
    }

}

//
//class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {
//    private lateinit var mMap: GoogleMap
//    private var currentMarker: Marker? = null
//    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
//    private var currentLocation: Location? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_google_maps)
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        fetchLocation()
//    }
//
//    private fun fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                1000
//            )
//            return
//        }
//
//        val task = fusedLocationProviderClient?.lastLocation
//        task?.addOnSuccessListener { location ->
//            if (location != null) {
//                this.currentLocation = location
//                Log.d("curlocation", currentLocation.toString())
//                val mapFragment =
//                    supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
//                mapFragment.getMapAsync(this)
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            1000 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                fetchLocation()
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        Log.d("MapDebug", "onMapReady called")
//        if (currentLocation != null) {
//            // Add a marker at the current location
//            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
//            drawMarker(latLng)
//        }
//    }
//
//    private fun drawMarker(latLng: LatLng) {
//        val marker = MarkerOptions()
//            .position(latLng)
//            .title("I am here")
//            .snippet(getAddress(latLng.latitude, latLng.longitude))
//            .draggable(true)
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//
//        // Remove the previous marker if it exists
//        currentMarker?.remove()
//
//        // Add the new marker to the map
//        currentMarker = mMap.addMarker(marker)
//        currentMarker?.showInfoWindow()
//    }
//
//    private fun getAddress(lat: Double, lon: Double): String {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        try {
//            val addresses = geocoder.getFromLocation(lat, lon, 1)
//            if (addresses!!.isNotEmpty()) {
//                return addresses!![0].getAddressLine(0) ?: ""
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return ""
//    }
//}









