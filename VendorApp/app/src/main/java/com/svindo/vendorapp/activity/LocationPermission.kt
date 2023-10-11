package com.svindo.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.ActivityLocationPermissionBinding
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast

@SuppressLint("Registered")
class LocationPermission : AppCompatActivity() {
    private lateinit var binding: ActivityLocationPermissionBinding
    //private lateinit var sharedPreference: SharedPreference
  //  private lateinit var statusResponse: StatusResponse
    private val foregroundRequestCode = 1
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPermissionBinding.inflate(layoutInflater);
        setContentView(binding.root)
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
        binding.allow.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), foregroundRequestCode)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), foregroundRequestCode)
                    }
                } else {
                    if (gpsStatus()) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        startActivity(Intent(this, TurnOnGPS::class.java))
                    }
                }
            }else{
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            foregroundRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        if (gpsStatus()) {
                           startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            startActivity(Intent(this, TurnOnGPS::class.java))

                        }
                    }
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            foregroundRequestCode
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            foregroundRequestCode
                        )
                    }
                }
                return
            }
        }
    }

    private fun gpsStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



    private fun handleLocationUpdates() {
        if (gpsStatus()) {
           startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, TurnOnGPS::class.java))
        }
    }


    private fun handleForegroundLocationUpdates() {
        if(!gpsStatus()){
           startActivity(Intent(this, MainActivity::class.java))
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