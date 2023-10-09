package com.svindo.vendorapp.activity


import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.svindo.vendorapp.R
import com.svindo.vendorapp.R.*
import com.svindo.vendorapp.databinding.ActivityEditBusinessdetailsBinding
import com.svindo.vendorapp.databinding.ActivityGoogleMapsBinding
import com.svindo.vendorapp.utils.SharedPreference
import java.io.IOException
import java.util.*

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnCameraIdleListener {
    private lateinit var mapsBinding: ActivityGoogleMapsBinding
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var sharedPreference: SharedPreference
    private var locationUpdateState = false
    private var marker: Marker? = null

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val REQUEST_CHECK_SETTINGS = 2
    }
    var address=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsBinding=ActivityGoogleMapsBinding.inflate(layoutInflater)
        sharedPreference = SharedPreference(this)
        setContentView(mapsBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // fetchLocation()
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult){
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation!!
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
            }
        createLocationRequest()
    }

    internal fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        marker?.remove()
        marker = mMap.addMarker(markerOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 20f))
      //  Log.d("TAG", "placeMarkerOnMap: $marker")

    }

    private fun checkValidations() {
        if (mapsBinding.edtLocation.text.toString().trim().isEmpty()) {
            mapsBinding.edtLocation.error = "Enter Location"
        } else {
            if(gpsStatus()) {
                requestPermissions()
            }else{
                showGPSDialog()
            }

        }
    }
    private fun gpsStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun showGPSDialog() {
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            /*locationUpdateState = true
            startLocationUpdates()*/
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @SuppressLint("LogConditional", "SetTextI18n")
    internal fun placeMarkerAtCurrentLocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        val markerOptions = MarkerOptions().position(latLng).title("My Location")
                        marker = mMap.addMarker(markerOptions)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f)) // Zoom level can be adjusted
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure to get location
                }
        } else {
            // Request location permission if not granted
            // Handle the permission request, typically by using ActivityCompat.requestPermissions()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.setOnCameraIdleListener(this)
        setUpMap()

        placeMarkerAtCurrentLocation()
    }

//    private fun createMarker(latLng: LatLng) {
//        val markerOptions = MarkerOptions().position(latLng)
//        marker = mMap.addMarker(markerOptions)
//    }

    override fun onCameraIdle() {
        // Fetch the location details when the map camera stops moving
        val cameraPosition = mMap.cameraPosition.target
        fetchLocationDetails(cameraPosition)
        updateMarkerPosition(cameraPosition)
    }

    internal fun updateMarkerPosition(latLng: LatLng) {
      //  marker?.position = latLng
        if (marker == null) {
            // Create a new marker if it doesn't exist
            val markerOptions = MarkerOptions().position(latLng)
            marker = mMap.addMarker(markerOptions)
        } else {
            // Animate the marker to the new position
            val currentLatLng = marker!!.position
            val startPosition = currentLatLng.latitude to currentLatLng.longitude
            val endPosition = latLng.latitude to latLng.longitude

            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 100 // Animation duration in milliseconds
            valueAnimator.addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val newPosition = LatLng(
                    startPosition.first + fraction * (endPosition.first - startPosition.first),
                    startPosition.second + fraction * (endPosition.second - startPosition.second)
                )
                marker!!.position = newPosition
            }

            valueAnimator.start()
        }
    }

    private fun fetchLocationDetails(latLng: LatLng) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses!!.isNotEmpty()) {
                 address = addresses[0].getAddressLine(0)
                val locationDetails = "${latLng.latitude} ${latLng.longitude}"
                mapsBinding.edtLocation.setText(locationDetails)
                mapsBinding.location.setOnClickListener {
                    sharedPreference.save("latlong",locationDetails)
                    val i = Intent(this@GoogleMapsActivity, BusinessdetailsActivity::class.java)
                    startActivity(i)
                }
                // Display locationDetails or use it as needed
               // mapsBinding.edtLocation.setText(address)
            } else {
                // Handle the case where no address was found
                // For example, display a message to the user or perform some other action.
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle any errors that occur during geocoding, such as network issues.
        }
    }

    private fun setUpMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

    }



    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }
    private fun requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withActivity(this)
            // below line is use to request the number of permissions which are required in our app.
            .withPermissions(Manifest.permission.CAMERA,)
            // after adding permissions we are calling an with listener method.
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    // this method is called when all permissions are granted
                    // check for permanent denial of any permission
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently, we will show user a dialog message.
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    // this method is called when user grants some permission and denies some of them.
                    permissionToken!!.continuePermissionRequest()
                }
            }).withErrorListener {
                // we are displaying a toast message for error message.
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            // below line is use to run the permissions on same thread and to check the permissions
            .onSameThread().check()
    }

    internal fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        val builder = AlertDialog.Builder(this@GoogleMapsActivity)

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions")

        // below line is our message for our dialog
        builder.setMessage("${getString(R.string.app_name)} needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel()
            // below is the intent from which we are redirecting our user.

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // this method is called when user click on negative button.
            dialog.cancel()
        }
        // below line is used to display our dialog
        builder.show()
    }


    override fun onMarkerClick(p0: Marker): Boolean = false

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //2
        //fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }
}

//    private fun fetchLocation() {
//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
//            return
//        }
//
//       val task = fusedLocationClient?.lastLocation
//        task?.addOnSuccessListener { location ->
//            if(location != null) {
//                this.currentLocation=location
//                Log.d("curlocation",currentLocation.toString())
//                val mapFragment = supportFragmentManager.findFragmentById(id.mapfragment) as SupportMapFragment
//                 mapFragment.getMapAsync(this)
//            }
//        }
//    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            1000 -> if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                fetchLocation()
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        Log.d("MapDebug", "onMapReady called")
//        // Add a marker at an initial location
//            val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
//            drawMarker(latlong)
//        // Set up a marker drag listener
//       currentmarker?.isDraggable = true
//        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
//            override fun onMarkerDrag(p0: Marker) {
////                val updatedLocation = p0?.position
////                if (updatedLocation != null) {
////                    val latitude = updatedLocation.latitude
////                    val longitude = updatedLocation.longitude
////                    // Do something with the updated location (e.g., display it)
////                    // latitude and longitude now contain the new location
////                }
//            }
//
//            override fun onMarkerDragEnd(p0: Marker) {
//                if(currentmarker !=null)
//                    currentmarker?.remove()
//                val newLatLng = LatLng(p0?.position!!.latitude,p0?.position!!.longitude)
//                drawMarker(newLatLng)
//            }
//
//            override fun onMarkerDragStart(p0: Marker) {
//
//            }
//        })
//    }
//
//    internal fun drawMarker(latlong:LatLng){
//        val marker= MarkerOptions()
//            .position(latlong)
//            .title("I am here")
//            .snippet(getAddress(latlong.latitude,latlong.longitude))
//            .draggable(true)
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
//        currentmarker=mMap.addMarker(marker)
//        currentmarker?.showInfoWindow()
//    }
//
//
//    private fun getAddress(lat:Double,lon:Double): String? {
//        val geocoder= Geocoder(this, Locale.getDefault())
//        val addresses=geocoder.getFromLocation(lat,lon,1)
//        return addresses!![0].getAddressLine(0).toString()
//    }
//
//}








