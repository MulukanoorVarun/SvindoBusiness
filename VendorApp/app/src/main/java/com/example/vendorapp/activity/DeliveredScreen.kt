package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.AcceptOrderAdapter
import com.example.vendorapp.adapters.ViewAddonsAdapter
import com.example.vendorapp.databinding.AcceptorderlayoutdesignBinding
import com.example.vendorapp.databinding.ActivityDeliveredScreenBinding
import com.example.vendorapp.databinding.ActivityShipOrderScreenBinding
import com.example.vendorapp.fragements.HomeFragment
import com.example.vendorapp.modelclass.OrderAcceptModal
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.modelclass.ViewAddonsListModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("StaticFieldLeak")
private lateinit var deliverOrderBinding: ActivityDeliveredScreenBinding
class DeliveredScreen : AppCompatActivity() {

    private lateinit var Binding: AcceptorderlayoutdesignBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var deliverresponse: OrderAcceptModal
    private lateinit var orderstatusresponse: Verify_otp_Response
    private lateinit var addonsresponse: ViewAddonsListModal
    lateinit var adapter: AcceptOrderAdapter
    lateinit var adapter1: ViewAddonsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deliverOrderBinding = ActivityDeliveredScreenBinding.inflate(layoutInflater)


        sharedPreference = SharedPreference(this)
        setContentView(deliverOrderBinding.root)

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager1= LinearLayoutManager(this)

        deliverOrderBinding.recyclerview.layoutManager = linearLayoutManager
        deliverOrderBinding.recyclerview.hasFixedSize()

        deliverOrderBinding.addonsrecyclerview.layoutManager = linearLayoutManager1
        deliverOrderBinding.addonsrecyclerview.hasFixedSize()

        val loginButton = findViewById<ImageView>(R.id.deliverbackbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        deliverOrderBinding.deliverbutton.setOnClickListener {
            val i = Intent(this@DeliveredScreen,MainActivity::class.java)
            startActivity(i)
        }

        Deliveredorder()
        OrderStatus()
    }
    fun Deliveredorder() {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AcceptOrdersDetails(sharedPreference.getValueString("token"),"515")
            requestCall.enqueue(object : Callback<OrderAcceptModal> {
                override fun onResponse(
                    call: Call<OrderAcceptModal>,
                    response: Response<OrderAcceptModal>
                ) {
                    try {
                        when {
                            response.code() == 200 -> {
                                deliverresponse = response.body()!!
                                //deliverOrderBinding.otptext.text = deliverresponse.order_details.otp
                                deliverOrderBinding.name.text = deliverresponse.order_details.user_name
                                deliverOrderBinding.addresstxt.text = deliverresponse.order_details.customer_address
                                deliverOrderBinding.Orderdate.text = deliverresponse.order_details.order_date
                                //shiporderOrderBinding.grandtotal.text = acceptorderresponse.order_details.net_payable_amount
                                deliverOrderBinding.orderamount.text = deliverresponse.order_details.order_amount
                                deliverOrderBinding.orderstatus.text = deliverresponse.order_details.order_status
                                deliverOrderBinding.orderno.text = deliverresponse.order_details.order_number

//                                OrderAddons(id:String)


                                if (deliverresponse.list.isNotEmpty()) {
                                    deliverOrderBinding.recyclerview.visibility = View.VISIBLE
                                    //  acceptOrderBinding.noData.visibility = View.GONE
                                    adapter = AcceptOrderAdapter(deliverresponse.list, applicationContext)

                                    deliverOrderBinding.recyclerview.adapter = adapter
                                    for (item in deliverresponse.list) {
                                        // body of loop
                                        OrderAddons(item.id);
                                    }
                                } else {
                                    deliverOrderBinding.recyclerview.visibility = View.GONE
                                    // acceptOrderBinding.noData.visibility = View.VISIBLE

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

                override fun onFailure(call: Call<OrderAcceptModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }
    fun OrderStatus() {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.OrderStatusDeatils(sharedPreference.getValueString("token"),"2","pending")
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                orderstatusresponse = response.body()!!


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


    fun OrderAddons(id:String) {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.OrderAddonsDeatils(sharedPreference.getValueString("token"),id)
            requestCall.enqueue(object : Callback<ViewAddonsListModal> {
                override fun onResponse(
                    call: Call<ViewAddonsListModal>,
                    response: Response<ViewAddonsListModal>
                ) {

                    try {
                        when {
                            response.code() == 200 -> {

                                addonsresponse = response.body()!!
                                if (addonsresponse.addon_list.isNotEmpty()) {
                                    deliverOrderBinding.addonsrecyclerview.visibility = View.VISIBLE
                                    //  acceptOrderBinding.noData.visibility = View.GONE
                                    adapter1 = ViewAddonsAdapter(addonsresponse.addon_list, applicationContext)

                                    deliverOrderBinding.addonsrecyclerview.adapter = adapter1
                                } else {
                                    deliverOrderBinding.addonsrecyclerview.visibility = View.GONE
                                    // acceptOrderBinding.noData.visibility = View.VISIBLE

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

                override fun onFailure(call: Call<ViewAddonsListModal>, t: Throwable) {
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