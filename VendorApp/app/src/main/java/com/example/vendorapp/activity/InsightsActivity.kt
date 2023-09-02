package com.example.vendorapp.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.*
import com.example.vendorapp.databinding.ActivityInsightsBinding
import com.example.vendorapp.modelclass.InsightsModal
import com.example.vendorapp.modelclass.InsightsModalClass
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var insightsBinding: ActivityInsightsBinding
class InsightsActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    lateinit var insightsResponse: InsightsModalClass
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var adapter: InsightsAdapter
    private lateinit var adaptertop: TopratedAdapter
    private lateinit var adaptermost: MostByAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        insightsBinding = ActivityInsightsBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(insightsBinding.root)

        sharedPreference= SharedPreference(this)



        val loginButton = findViewById<ImageView>(R.id.backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager1 = LinearLayoutManager(this)
        linearLayoutManager2= LinearLayoutManager(this)



       linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        insightsBinding.horizontalrv.layoutManager = linearLayoutManager
        insightsBinding.horizontalrv.hasFixedSize()

        linearLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        insightsBinding.horizontalrv1.layoutManager = linearLayoutManager1
        insightsBinding.horizontalrv1.hasFixedSize()

        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        insightsBinding.horizontalrv2.layoutManager = linearLayoutManager2
        insightsBinding.horizontalrv2.hasFixedSize()

        InsightsDetails()
    }

    fun InsightsDetails()
    {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.InsightsDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<InsightsModalClass> {
                override fun onResponse(
                    call: Call<InsightsModalClass>,
                    response: Response<InsightsModalClass>
                ) {
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                insightsResponse = response.body()!!
                                insightsBinding.shopviewsno.text = insightsResponse.shopviews
                                insightsBinding.followersno.text = insightsResponse.followers
                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        if (insightsResponse.toplike.isNotEmpty()) {
                                            insightsBinding.horizontalrv.visibility = View.VISIBLE
                                            //insightsBinding.noData.visibility = View.GONE
                                            adapter = InsightsAdapter(insightsResponse.toplike, applicationContext)
                                            insightsBinding.horizontalrv.adapter = adapter
                                        } else {
                                            insightsBinding.horizontalrv.visibility = View.GONE
                                            // ActivityInsightsBinding.noData.visibility = View.VISIBLE
                                        }
                                        if (insightsResponse.toprated.isNotEmpty()) {
                                            insightsBinding.horizontalrv1.visibility = View.VISIBLE
                                            adaptertop = TopratedAdapter(insightsResponse.toprated,applicationContext)
                                            insightsBinding.horizontalrv1.adapter = adaptertop
                                        } else {
                                            insightsBinding.horizontalrv1.visibility = View.GONE
                                        }
                                        if (insightsResponse.mostbuy.isNotEmpty()) {
                                            insightsBinding.horizontalrv2.visibility = View.VISIBLE
                                            adaptermost = MostByAdapter(insightsResponse.mostbuy, applicationContext)
                                            insightsBinding.horizontalrv2.adapter = adaptermost
                                        } else {
                                            insightsBinding.horizontalrv2.visibility = View.GONE
                                        }
                                    }else{

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

                override fun onFailure(call: Call<InsightsModalClass>, t: Throwable) {
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


