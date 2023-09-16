
package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.svindo.vendorapp.R
import com.svindo.vendorapp.adapters.CustomerFeedbackAdapter
import com.svindo.vendorapp.databinding.ActivityCustomerFeedbackBinding
import com.svindo.vendorapp.modelclass.CustomerFeedbackModal
import com.svindo.vendorapp.services.ApiClient
import com.svindo.vendorapp.services.ApiInterface
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("Registered")
class CustomerFeedback : AppCompatActivity() {
    private lateinit var feedbackBinding: ActivityCustomerFeedbackBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var feedbackresponse: CustomerFeedbackModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CustomerFeedbackAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        feedbackBinding = ActivityCustomerFeedbackBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(feedbackBinding.root)


        val loginButton = findViewById<ImageView>(R.id.feedbackbackbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        feedbackBinding.feedbackrv.layoutManager = linearLayoutManager
        feedbackBinding.feedbackrv.hasFixedSize()

        CustomerFeebackdetails()
    }


    fun CustomerFeebackdetails() {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.Customerfeedbackdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<CustomerFeedbackModal> {
                override fun onResponse(
                    call: Call<CustomerFeedbackModal>,
                    response: Response<CustomerFeedbackModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                feedbackresponse = response.body()!!

                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        if (feedbackresponse.data.isNotEmpty()) {
                                            feedbackBinding.feedbackrv.visibility = View.VISIBLE
                                            // feedbackBinding.noData.visibility = View.GONE
                                            adapter = CustomerFeedbackAdapter(
                                                feedbackresponse.data,
                                                applicationContext
                                            )
                                            feedbackBinding.feedbackrv.adapter = adapter
                                        } else {
                                            feedbackBinding.feedbackrv.visibility = View.GONE
                                            //  feedbackBinding.noData.visibility = View.VISIBLE

                                        }
                                    }
                                }


                            }
                            response.code() == 401 -> {
                                showToast(getString(R.string.session_exp))

                            }
                            else -> {
                                showToast(getString(R.string.server_error))
                            }
                        }


                    } catch (e: TimeoutException) {
                        showToast(getString(R.string.time_out))
                    }
                }

                override fun onFailure(call: Call<CustomerFeedbackModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
}

