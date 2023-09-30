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
        setContentView(R.layout.activity_google_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }
    private fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
       val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if(location != null){
                this.currentLocation=location
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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

        // Add a marker at an initial location
        currentLocation?.let { location ->
            val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
            drawMarker(latlong)
        }
//        marker = mMap.addMarker(MarkerOptions().position(latlong).title("Marker"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))

        // Set up a marker drag listener
       // currentmarker?.isDraggable = true
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
                val newLatLng= LatLng(p0?.position!!.latitude,p0?.position!!.longitude)
                drawMarker(newLatLng)
            }

            override fun onMarkerDragStart(p0: Marker) {
            }
        })
        mMap.setOnCameraMoveListener {
            // Get the center of the map's camera position
            val cameraPosition = mMap.cameraPosition.target

            // Remove the current marker if it exists
            currentmarker?.remove()

            // Place a new marker at the camera's position
            drawMarker(cameraPosition)
        }
    }

    internal fun drawMarker(latlong:LatLng){
        val marker= MarkerOptions().position(latlong).title("I am here")
            .snippet(getAddress(latlong.latitude,latlong.longitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
        currentmarker=mMap.addMarker(marker)
        currentmarker?.showInfoWindow()
    }

    private fun getAddress(lat:Double,lon:Double): String? {
        val geocoder= Geocoder(this, Locale.getDefault())
        val addresses=geocoder.getFromLocation(lat,lon,1)
        return addresses?.get(0)?.getAddressLine(0).toString()

//        val addresses: List<Address>?
//        try {
//            addresses = geocoder.getFromLocation(lat, lon, 1)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return "Address not available due to network issue"
//        }
//
//        if (addresses != null && addresses.isNotEmpty()) {
//            val address = addresses[0]
//            val addressParts = ArrayList<String>()
//
//            // Collect address parts such as locality, postal code, etc.
//            if (address.locality != null) {
//                addressParts.add(address.locality)
//            }
//            if (address.postalCode != null) {
//                addressParts.add(address.postalCode)
//            }
//            if (address.countryName != null) {
//                addressParts.add(address.countryName)
//            }
//
//            // Concatenate the address parts
//            return addressParts.joinToString(", ")
//        }
//
//        return "Address not found"
    }
}



//class GoogleMapsActivity : AppCompatActivity(){
//    private lateinit var googleMap: GoogleMap
//    private lateinit var mapView: MapView
//    private var marker: Marker? = null
//    private var currentLocation: LatLng? = null // Store
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?){
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_google_maps)
//    }
//}


