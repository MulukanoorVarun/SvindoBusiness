package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import com.example.vendorapp.R
import com.example.vendorapp.databinding.ActivityOtpveryfiyBinding
import com.example.vendorapp.modelclass.Mobileotp_Response
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
lateinit var otpverifybinding: ActivityOtpveryfiyBinding
class Otpveryfiy_Activity : AppCompatActivity() {
    private val TOKEN = "token"
    private lateinit var verifyotpResponse: Verify_otp_Response
    private lateinit var mobile_number: String
    private lateinit var genrateotpresponse: Mobileotp_Response
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        val i = intent
        val mobile_number = i.getStringExtra("TextBox")
        val otp = intent.getStringExtra("otpcode")
//        val otpCharArray = otp?.toCharArray()
//        binding.firstPinView.setText(otp)

        otpverifybinding = ActivityOtpveryfiyBinding.inflate(layoutInflater)
        setContentView(otpverifybinding.root)
        otpverifybinding.editmobileNumbertxt.setText(mobile_number)


        otpverifybinding.editmobileNumbertxt.setOnClickListener {

            val i = Intent(this@Otpveryfiy_Activity, LoginActivity::class.java)
            startActivity(i)

        }
        otpverifybinding.firstPinView.setText(otp)
        otpverifybinding.vrfiyBtn.setOnClickListener {
            if (otpverifybinding.firstPinView.text.toString().trim().isEmpty()) {
                showToast("OTP should be of minimum of 6 numbers")
                otpverifybinding.firstPinView.error = "Enter otp"
            } else if (otpverifybinding.firstPinView.text.toString().trim().length != 6) {

                showToast("OTP should be of minimum of 6 numbers")
            } else if (otpverifybinding.firstPinView.text.toString().trim().length == 6) {
                val otp: String = otpverifybinding.firstPinView.text.toString()


                if (mobile_number != null) {
                    verifyotp(
                        mobile_number,
                        otpverifybinding.firstPinView.text.toString().trim(),


                        )
                }

            }

        }
        otpverifybinding.resendTxtview.setOnClickListener {


            if (mobile_number != null) {
                genotp(mobile_number)
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
                            genrateotpresponse = response.body()!!

//                            if (genrateotpresponse.error==0)
//                            {
                                showToast(genrateotpresponse.otp.toString()); showToast(genrateotpresponse.message);
//                            }

//                            val responseMessage = response.body()?.error
//                            println(responseMessage);

//                            otpverifybinding.firstPinView.text=null

//                            val i = Intent(this@MainActivity, OTPscreen::class.java)
//                            i.putExtra("TextBox", com.example.deliverypartner.binding.mobileNumberEtxt.text.toString());
//                            response.body()?.let { i.putExtra("otpcode", it.otp) };
//                            startActivity(i)

                        }

//                        Log.d("TAG", "onResponse: " + (response.body()?.otp))
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

    private fun verifyotp(mobile_number: String, otp: String) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.Verify_otp(mobile_number, otp)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {

            //if you receive http response then this method will executed
            //your status code decide if your http response is a success or failure
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        if (response.isSuccessful) {

                            verifyotpResponse = response.body()!!

                            response.body()?.let { showToast(it.message)}
                                if(verifyotpResponse!= null) {
                                    if (response.body()!!.error == "0") {
                                        sharedPreference.save("token", verifyotpResponse.token);
                                        response.body()?.let { showToast(it.message)}
                                        val intent = Intent(
                                            this@Otpveryfiy_Activity, DashboardActivity::class.java
                                        )
                                        startActivity(intent)
                                    }else if (response.body()?.error == "2") {
                                        sharedPreference.save("mobile_number", (mobile_number).toString());
                                        val intent = Intent(
                                            this@Otpveryfiy_Activity, BusinessdetailsActivity::class.java
                                        )
                                        startActivity(intent)
                                    }else {
                                        response.body()?.let { showToast(it.message)}
                                    }
                                }
                        }
//                        Log.d("TAG", "onResponse: " + (response.body()?.error))
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

            override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {

                showToast(getString(R.string.session_exp))
            }

        })



        }

    }



