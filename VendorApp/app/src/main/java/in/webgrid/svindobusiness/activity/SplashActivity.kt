package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import`in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.Utils.SharedPreference


@SuppressLint("CustomSplashScreen", "Registered")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
//    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//    private val permissionRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreference(this)

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

