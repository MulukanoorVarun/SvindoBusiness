package `in`.webgrid.svindobusiness.services


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import `in`.webgrid.svindobusiness.activity.MainActivity
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.isNetworkAvailable

import java.lang.Exception
import java.util.*

@SuppressLint("Registered")
class LocationService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val REQUEST_CHECK_SETTINGS = 2
    private lateinit var sharedPreference: SharedPreference
    private lateinit var input: String
    var mTimer: Timer? = null
    private var notify_interval: Long = 30000
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, LocationService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context, message: String) {
            val stopIntent = Intent(context, LocationService::class.java)
            stopIntent.putExtra("inputExtra", message)
            context.stopService(stopIntent)
        }
    }

    @SuppressLint("LogConditional")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//android:foregroundServiceType="location"
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable {
            requestLocationUpdates()
            mHandler.postDelayed(
                mRunnable, // Runnable
                10000 // Delay in milliseconds
            )
        }
        // Schedule the task to repeat after 1 second
        mHandler.postDelayed(
            mRunnable, // Runnable
            1000 // Delay in milliseconds
        )
        sharedPreference = SharedPreference(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //do heavy work on a background thread
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation!!
                val s = "${lastLocation.latitude},${lastLocation.longitude}"
              //  WebSocketManager .sendMessage( "{ \"loc\":\"$s\"}" )
/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        saveLocations(
                            sharedPreference.getValueString("sessionId")!!,
                            "${lastLocation.latitude},${lastLocation.longitude}",
                            lastLocation.speed,
                            lastLocation.altitude,
                            lastLocation.bearing,
                            lastLocation.bearingAccuracyDegrees,
                            lastLocation.verticalAccuracyMeters,
                            lastLocation.speedAccuracyMetersPerSecond,
                            lastLocation.accuracy,
                            lastLocation.provider
                        )
                } else {
                        saveLocations( sharedPreference.getValueString("sessionId")!!,
                            "${lastLocation.latitude},${lastLocation.longitude}", 0.0F,
                            0.0, 0.0F, 0.0F,
                            0.0F, 0.0F,
                            0.0F, ""
                        )
                }*/

            }
        }
        createLocationRequest()

        createNotificationChannel()
        input = if (!gpsStatus()) {
            "You're Offline !, Check your GPS connection."
        } else if (!isNetworkAvailable()) {
            "You're Offline !, Check your network connection."
        } else {
            "You're Online !"
        }
        Log.d("TAG", "onLocationResultinput: $input")
        showNotification(input)
        return START_NOT_STICKY
    }



    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.setSound(null, null)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun showNotification(input: String) {
        val notificationIntent = Intent(this, MainActivity::class.java).putExtra("flag",1)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK}
                    val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("svindo vendor")
            .setContentText(input)
            .setStyle(NotificationCompat.BigTextStyle().bigText(input))
          //  .setSmallIcon(R.drawable.ic_launcher)
            .setGroup("Foreground Notifications")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(101, notification)
    }

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { /*ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)*/
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
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

        /* task.addOnFailureListener { e ->
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
             }*/
    }

    private fun requestLocationUpdates() {
        if (!gpsStatus()) {
            showNotification("You're Offline !, Check your GPS connection.")
        } else {
            createLocationRequest()
        }
    }


    private fun gpsStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onDestroy() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }catch (e: Exception){
        }
        super.onDestroy()
    }
}


/* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
     if (requestCode == REQUEST_CHECK_SETTINGS) {
         if (resultCode == Activity.RESULT_OK) {
             locationUpdateState = true
             startLocationUpdates()
         }
     }
 }*/


