package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.Utils.SharedPreference


@SuppressLint("CustomSplashScreen", "Registered")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
//    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//    private val permissionRequestCode = 123
 val CHANNEL_ID = "in.webgrid.svindobusiness"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreference(this)

        if (isNotificationChannelCreated(CHANNEL_ID)) {
            Log.d("SplashActivity", "Notification channel already exists")
        } else {
            Log.d("SplashActivity", "Creating notification channel")
            // Create the notification channel
            createNotificationChannel()
        }

//        if (checkPermissions()) {
//            startNextActivity()
//        } else {
//            requestPermissions()
//        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 milliseconds

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply{
                description = channelDescription
            }
//            channel.setSound(alarmSound, null)
            //Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("SplashActivity", "Notification channel created")
        }
    }

    private fun isNotificationChannelCreated(channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            val channel = notificationManager.getNotificationChannel(channelId)
            return channel != null
        }
        return false // Notification channels are not supported on versions prior to Oreo
    }


//    private fun checkPermissions(): Boolean {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission)
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                return false
//            }
//        }
//        return true
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == permissionRequestCode) {
//            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                // All permissions granted
//                startNextActivity()
//            } else {
//                // Permissions not granted
//                // Handle this case, e.g., show an error message or exit the app
//            }
//        }
//    }
//
//    private fun startNextActivity() {
//        // Start your main activity or the next activity after obtaining permissions
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}

