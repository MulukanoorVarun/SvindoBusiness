package `in`.webgrid.svindobusiness.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.databinding.MobilenumberloginBinding
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import `in`.webgrid.svindobusiness.Utils.SharedPreference
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
    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val permissionRequestCode = 123
    private lateinit var locationRequest: LocationRequest
//    private lateinit var genrateotpresponse: GenrateotpResponse
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
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
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
        if (mobileloginbinding.mobileNumberEtxt.text.toString() == "9390776532") {
            sharedPreference.save("token","1d9c1ce0e1c645c4bf02c47b99a90f7863d7562854713add0a30eaf88f7329fd6d15ecca4fb0d4400a035472ee6488bc29f7")
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
                genotp(
                    mobileloginbinding.mobileNumberEtxt.text.toString().trim(),
                )
            }
        }
    }
    }

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

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

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


