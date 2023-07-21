package com.example.vendorapp.activity

import com.example.vendorapp.utils.showToast
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vendorapp.R
import com.example.vendorapp.modelclass.Bankdetails_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.services.SessionManager
import com.example.vendorapp.databinding.ActivityBankaccountBinding
import com.example.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var bankaccountdetailsbinding: ActivityBankaccountBinding
private lateinit var bankdetailsResponse:Bankdetails_Response
class BankaccountActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        bankaccountdetailsbinding = ActivityBankaccountBinding.inflate(layoutInflater)
        setContentView(bankaccountdetailsbinding.root)
        //sharedPreference = SharedPreference(this)
        val Sessionid = intent.getStringExtra("token")

        bankaccountdetailsbinding.bankdetailsBackbutton.setOnClickListener {

            val intent = Intent(this, PendingDocuments::class.java)
            startActivity(intent)
        }







        bankaccountdetailsbinding.bankdetailsSubmitbutton.setOnClickListener {


            val ifsc_code = bankaccountdetailsbinding.ifscEtTxt.text.toString().trim()
            val bank_name = bankaccountdetailsbinding.bankNameEtTxt.text.toString().trim()
            val account_number = bankaccountdetailsbinding.accNumEtTxt.text.toString().trim()
            val accountreenternum = bankaccountdetailsbinding.reAccNumEtTxt.text.toString().trim()

            if (ifsc_code.isNotEmpty() && bank_name.isNotEmpty() && account_number.isNotEmpty() && accountreenternum.isNotEmpty()) {


                bankaccountdetails(
                    bankaccountdetailsbinding.bankNameEtTxt.text.toString().trim(),
                    bankaccountdetailsbinding.ifscEtTxt.text.toString().trim(),
                    bankaccountdetailsbinding.accNumEtTxt.text.toString().trim(),
                )


            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun bankaccountdetails(
        bank_name: String,
        ifsc_code: String,
        account_number: String,

        ) {

        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.bankaccountdetails(sharedPreference.getValueString("token"),"bank_account",bank_name,ifsc_code,account_number)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299

                        if (response.isSuccessful) {
                            showToast(""+response.body())
                            response.body()?.let { showToast(it.message)}
                            bankdetailsResponse = response.body()!!
//                            getPreferences(MODE_PRIVATE).edit().putString("error",bankdetailsResponse.error).apply();

                            val i = Intent(this@BankaccountActivity,ContactActivity::class.java)
                            startActivity(i)





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

            override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {

                showToast(getString(R.string.session_exp))
            }

        })

    }
}