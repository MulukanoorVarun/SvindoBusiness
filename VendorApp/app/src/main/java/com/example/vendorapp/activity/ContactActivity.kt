package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vendorapp.R
import com.example.vendorapp.databinding.ActivityContactBinding
import com.example.vendorapp.modelclass.Bankdetails_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var contactbinding : ActivityContactBinding
lateinit var contactResponse: Bankdetails_Response
class ContactActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        contactbinding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(contactbinding.root)


        contactbinding.button.setOnClickListener {

            val emergency_mobile_number = contactbinding.mobNumEtTxt.text.toString().trim()
            val emergency_contact_name = contactbinding.nameEtTxt.text.toString().trim()




            if (emergency_contact_name.isNotEmpty() && emergency_mobile_number.isNotEmpty() ) {


                contactdetails(

                    contactbinding.nameEtTxt.text.toString().trim(),
                    contactbinding.mobNumEtTxt.text.toString().trim(),

                )


            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }




        }

        contactbinding.contactbackbutton.setOnClickListener {
            val intent = Intent(this, PendingDocuments::class.java)
            startActivity(intent)
        }


    }

    private fun contactdetails(
        emergency_contact_name: String,
        emergency_mobile_number: String,
    ) {
        //println(SessionManager.getToken());
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.contactdetails(sharedPreference.getValueString("token"),"contact",emergency_contact_name, emergency_mobile_number)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {


            //if you receive http response then this method will executed
            //your status code decide if your http response is a success or failure
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        if (response.isSuccessful) {

//                            showToast(""+response.body())
                            response.body()?.let { showToast(it.message)}


                            val key = "MY_KEY"
                            val i = Intent(this@ContactActivity ,PancardActivity::class.java)
                            intent.putExtra("KEY_EXTRA", key)
                            startActivity(i)


                        }

//                        Log.d("TAG", "onResponse: " + (response.body()?.error))
                        contactResponse = response.body()!!

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

            override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {

                showToast(getString(R.string.session_exp))
            }

        })






    }


//    private fun contactdetails(
//        bank_name: String,
//        ifsc_code: String,
//        account_number: String,
//
//        ) {
//
//        val loginService = ApiClient.buildService(ApiInterface::class.java)
//        var name=contactbinding.nameEtTxt.text.toString().trim()
//        var num=contactbinding.mobNumEtTxt.text.toString().trim()
//        val requestCall = loginService.contactdetails(sharedPreference.getValueString("token"),"bank_account",name,num)
//        requestCall.enqueue(object : Callback<Bankdetails_Response> {
//            @SuppressLint("SuspiciousIndentation")
//            override fun onResponse(
//                call: Call<Bankdetails_Response>,
//                response: Response<Bankdetails_Response>
//            ) {
//                when {
//                    response.isSuccessful -> {//status code between 200 to 299
//
//                        if (response.isSuccessful) {
//                            showToast(""+response.body())
//                            response.body()?.let { showToast(it.message)}
//                            contactResponse = response.body()!!
////                            getPreferences(MODE_PRIVATE).edit().putString("error",bankdetailsResponse.error).apply();
//
////                            val i = Intent(this@ContactActivity,ContactActivity::class.java)
////                            startActivity(i)
//
//
//
//
//
//                        }
//
////                        Log.d("TAG", "onResponse: " + (response.body()?.error))
//
//
//                    }
//
//                    response.code() == 401 -> {//unauthorised
//                        showToast(getString(R.string.session_exp))
//                    }
//                    else -> {//Application-level failure
//                        //status code in the range of 300's, 400's, and 500's
//                        showToast(getString(R.string.server_error))
//                    }
//                }
//
//
//            }
//
//            override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
//
//                showToast(getString(R.string.session_exp))
//            }
//
//        })
//
//    }
    }
