package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.ActivityTurnOnGpsBinding
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast

@SuppressLint("Registered")
class TurnOnGPS : AppCompatActivity() {
    private lateinit var binding: ActivityTurnOnGpsBinding
    private lateinit var sharedPreference: SharedPreference
    //private lateinit var statusResponse: StatusResponse
    private lateinit var snackbar: Snackbar
    private lateinit var locationRequest: LocationRequest
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    val REQUEST_CHECK_SETTINGS = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTurnOnGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
        binding.onGPS.setOnClickListener {
            if(gpsStatus()){
                startActivity(Intent(this, MainActivity::class.java))
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
                        this, REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    //gps and battery permissions codes
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
              startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
               showGPSDialog()
            }
        }
    }

    var exit: Boolean = false
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (exit) {
            finishAffinity()
        } else {
            showToast("Press Back again to Exit from App.")
            exit = true
            Handler(Looper.getMainLooper()).postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }
}