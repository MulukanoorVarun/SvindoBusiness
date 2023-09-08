package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.vendorapp.R
import com.example.vendorapp.adapters.CustomSpinnerAdapter
import com.example.vendorapp.adapters.SpotlightSpinnerAdapter
import com.example.vendorapp.databinding.ActivityAddPrintingProductBinding
import com.example.vendorapp.databinding.ActivityAddSpotlightBinding
import com.example.vendorapp.databinding.AddspotlightlayoutBinding
import com.example.vendorapp.databinding.FragmentSpotlightBinding
import com.example.vendorapp.fragements.HomeFragment
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("Registered")
class AddSpotlightActivity : AppCompatActivity() {
    private lateinit var addspotlightBinding:ActivityAddSpotlightBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var spotlightResponse: Verify_otp_Response
    private lateinit var spinner: Spinner

    var ItemId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spotlight)
        sharedPreference= SharedPreference(this)
        addspotlightBinding = ActivityAddSpotlightBinding.inflate(layoutInflater)
        setContentView(addspotlightBinding.root)


        val loginButton = findViewById<ImageView>(R.id.AddSpotlightbackbutton)
        loginButton.setOnClickListener {
            this.onBackPressed()
        }
        ProductsList()
    }
    fun ProductsList(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.ProductDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductsModal> {
                override fun onResponse(
                    call: Call<ProductsModal>,
                    response: Response<ProductsModal>
                ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            setupSpinner(response.body()!!.products)

                                        } else {

                                        }
                                    }else{

                                    }
                                }else{

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

                override fun onFailure(call: Call<ProductsModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    internal fun setupSpinner(items:List<Product>){
        spinner = findViewById(R.id.spotlightspinnerview)
        val adapter = SpotlightSpinnerAdapter(this,items)
        spinner.adapter = adapter
        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                ItemId = selectedItem.id
                addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
                addspotlightBinding.submitbutton.setOnClickListener {
                    addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.button_loading_background);
                    addspotlightBinding.submitbutton.setEnabled(false)
                    Handler().postDelayed({
                        addspotlightBinding.submitbutton.setEnabled(true)
                        addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
                    }, 2000)

                    fetchItemDetails(ItemId)
                }
                // Do something with the selected item (e.g., display its ID or name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun fetchItemDetails(itemId: String) {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AddSpotlight(sharedPreference.getValueString("token"),itemId)
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                spotlightResponse = response.body()!!
                                if (spotlightResponse.error=="0") {
                                    val i = Intent(this@AddSpotlightActivity, MainActivity::class.java)
                                    startActivity(i)
                                    showToast(spotlightResponse.message)
                                }
                                else {
                                   showToast(spotlightResponse.message)
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
                override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
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