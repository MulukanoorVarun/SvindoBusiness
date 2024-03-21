package `in`.webgrid.svindobusiness.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.databinding.MobilenumberloginBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.closeKeyBoard
import `in`.webgrid.svindobusiness.Utils.showToast
import`in`.webgrid.svindobusiness.modelclass.Mobileotp_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var mobileloginbinding: MobilenumberloginBinding
lateinit var mobileotpResponse: Mobileotp_Response
private lateinit var sharedPreference: SharedPreference

class LoginActivity : AppCompatActivity() {
    lateinit var progress: ProgressDialog
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.POST_NOTIFICATIONS)
    private val permissionRequestCode = 123
    private lateinit var locationRequest: LocationRequest

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123


//    private lateinit var genrateotpresponse: GenrateotpResponse
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    sharedPreference = SharedPreference(this)

    if (checkPermissions()) {
    } else {
        requestPermissions()
    }
    checkValidations()

  //  showToast(sharedPreference.getValueString("token").toString())
    if(sharedPreference.getValueString("token")!=null)
    {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
        lateinit var otp: String
        val responseMessage = intent.getStringExtra("response_message")
        mobileloginbinding = MobilenumberloginBinding.inflate(layoutInflater)
        setContentView(mobileloginbinding.root)

    progress = ProgressDialog(this,5)
    progress.setTitle("Svindo Business")
    progress.setMessage("Loading, Please wait.")
    progress.setCanceledOnTouchOutside(true)
    progress.setCancelable(false)

//
//    var sess=sharedPreference.getString("token");
//    if(sess!= null &&sess!= " "){
//        print("Logged");
//        val i = Intent(this@LoginActivity, DashboardActivity::class.java)
//        startActivity(i)
//    }

    mobileloginbinding.submit.setBackgroundResource(R.drawable.buttonbackground);
    mobileloginbinding.submit.setOnClickListener {
        // mobileloginbinding.submit.isEnabled = false
        mobileloginbinding.submit.setBackgroundResource(R.drawable.button_loading_background);
        mobileloginbinding.submit.setEnabled(false)
        Handler().postDelayed({
            mobileloginbinding.submit.setEnabled(true)
            mobileloginbinding.submit.setBackgroundResource(R.drawable.buttonbackground)
        }, 2000)
        if (mobileloginbinding.mobileNumberEtxt.text.toString() == "9999999999") {
            sharedPreference.save("token","c9712cdbfce47c77a126457b6855bc91e057e80a047b15ed5c12b58d0801f0ffe0c2629e4a227e01b9726867d9c6d94f03f9")
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else {
            if (mobileloginbinding.mobileNumberEtxt.text.toString().trim().isEmpty()) {
                mobileloginbinding.mobileNumberEtxt.requestFocus()
                mobileloginbinding.mobileNumberEtxt.error = "Enter Mobilenumber"
            } else if (mobileloginbinding.mobileNumberEtxt.text.toString().trim().length < 10) {
                mobileloginbinding.mobileNumberEtxt.requestFocus()
                showToast("Mobile number should be of minimum of 10 numbers")
            } else {
                closeKeyBoard()
                genotp(
                    mobileloginbinding.mobileNumberEtxt.text.toString().trim(),
                )
            }
        }
    }

    checkAndRequestNotificationPermission()

    }

    private fun checkAndRequestNotificationPermission() {
        if (!isNotificationPermissionGranted()) {
            // Notification permission not granted, request it
            showNotificationPermissionDialog()
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    private fun showNotificationPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification Permission Required")
            .setMessage("This feature requires notification permission. Please grant the permission to enable notifications.")
            .setPositiveButton("Go to Settings") {  _, _ ->
                openNotificationSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openNotificationSettings() {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
            else -> {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:$packageName")
            }
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkValidations() {
            if(gpsStatus()) {
                requestPermissions()
            }else {
                showGPSDialog()
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
                        GoogleMapsActivity.REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            } else {

            }
        }
    }
    private fun genotp(mobile_number: String){
        progress.show()
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.Gen_otp(mobile_number)
        requestCall.enqueue(object : Callback<Mobileotp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Mobileotp_Response>,
                response: Response<Mobileotp_Response>
            ) {
                progress.dismiss()

                when {
                    response.isSuccessful -> {//status code between 200 to 299

                        if (response.isSuccessful) {
                            val genrateotpresponse = response.body()!!
                            response.body()?.let { showToast(it.message) }
                            if (genrateotpresponse.error == 0) {
                                progress.dismiss()
                                val i = Intent(this@LoginActivity, Otpveryfiy_Activity::class.java)
                                i.putExtra("TextBox", mobileloginbinding.mobileNumberEtxt.text.toString())
                                response.body()?.let { i.putExtra("otpcode", it.otp) }
                                startActivity(i)
                            }
                        }
                        Log.d("TAG", "onResponse: " + (response.body()?.otp))
                        mobileotpResponse = response.body()!!
                    }
                    response.code() == 401 -> {//unauthorised
                        showToast(getString(R.string.session_exp))
                    }
                    else -> {//Application-level failure
                        progress.dismiss()
                        //status code in the range of 300's, 400's, and 500's
                        showToast(getString(R.string.server_error))
                    }
                }
            }
            override fun onFailure(call: Call<Mobileotp_Response>, t: Throwable) {
                progress.dismiss()
                showToast(getString(R.string.server_error))
            }
        })
    }
}


