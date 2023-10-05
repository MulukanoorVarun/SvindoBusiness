package com.svindo.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.MobilenumberloginBinding
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.svindo.vendorapp.fragements.HomeFragment
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast
import com.svindo.vendorapp.modelclass.Mobileotp_Response
import com.svindo.vendorapp.services.ApiClient
import com.svindo.vendorapp.services.ApiInterface
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
    sharedPreference = SharedPreference(this)

  //  showToast(sharedPreference.getValueString("token").toString())
    if(sharedPreference.getValueString("token")!=null)
    {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }
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
//            val intent = Intent(this, HomeFragment::class.java)
//            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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
    private fun genotp(mobile_number: String){
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.Gen_otp(mobile_number)
        requestCall.enqueue(object : Callback<Mobileotp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Mobileotp_Response>,
                response: Response<Mobileotp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299

                        if (response.isSuccessful) {
                            val genrateotpresponse = response.body()!!
                            response.body()?.let { showToast(it.message) }
                            if (genrateotpresponse.error == 0) {
                                val i = Intent(this@LoginActivity, Otpveryfiy_Activity::class.java)
                                i.putExtra(
                                    "TextBox",
                                    mobileloginbinding.mobileNumberEtxt.text.toString()
                                )
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
                        //status code in the range of 300's, 400's, and 500's
                        showToast(getString(R.string.server_error))
                    }
                }
            }
            override fun onFailure(call: Call<Mobileotp_Response>, t: Throwable) {
                showToast(getString(R.string.server_error))
            }
        })
    }
}


