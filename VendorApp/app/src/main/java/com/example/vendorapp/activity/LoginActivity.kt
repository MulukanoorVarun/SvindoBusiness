package com.example.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.R
import com.example.vendorapp.databinding.MobilenumberloginBinding
import android.annotation.SuppressLint
import android.content.Intent

import android.util.Log
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import com.example.vendorapp.modelclass.Mobileotp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var mobileloginbinding: MobilenumberloginBinding
lateinit var mobileotpResponse: Mobileotp_Response
private lateinit var sharedPreference: SharedPreference
class LoginActivity : AppCompatActivity() {
//    private lateinit var genrateotpresponse: GenrateotpResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var otp: String
        val responseMessage = intent.getStringExtra("response_message")
        mobileloginbinding = MobilenumberloginBinding.inflate(layoutInflater)
        setContentView(mobileloginbinding.root)

//
//    var sess=sharedPreference.getString("token");
//    if(sess!= null &&sess!= " "){
//        print("Logged");
//        val i = Intent(this@LoginActivity, DashboardActivity::class.java)
//        startActivity(i)
//    }
    mobileloginbinding.submit.setOnClickListener {
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

    private fun genotp(mobile_number: String) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.Gen_otp(mobile_number)
        requestCall.enqueue(object : Callback<Mobileotp_Response> {
            //if you receive http response then this method will executed
            //your status code decide if your http response is a success or failure
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Mobileotp_Response>,
                response: Response<Mobileotp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299

                        if (response.isSuccessful) {
                            val genrateotpresponse = response.body()?.error
                            response.body()?.let { showToast(it.otp.toString()); showToast(it.message);  }
                            val i = Intent(this@LoginActivity, Otpveryfiy_Activity::class.java)
                            i.putExtra("TextBox", mobileloginbinding.mobileNumberEtxt.text.toString());
                            response.body()?.let { i.putExtra("otpcode", it.otp) };
                            startActivity(i)

                        }

                        Log.d("TAG", "onResponse: " + (response.body()?.otp))
                        mobileotpResponse = response.body()!!


                    }

                    response.code() == 401 -> {//unauthorised
                        showToast(getString(R.string.session_exp))
                    }
                    else -> {//Application-level failure
                        //status code in the range of 300's, 400's, and 500's
                        showToast(getString(R.string.server_error))
                    }
                }
            }



            //invoked in case of Network Error or Establishing connection with Server
            //or Error Creating Http Request or Error Processing Http Response
            override fun onFailure(call: Call<Mobileotp_Response>, t: Throwable) {
                showToast(getString(R.string.server_error))
            }
        })


    }




}


