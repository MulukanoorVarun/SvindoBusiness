package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.svindo.vendorapp.R
import com.svindo.vendorapp.adapters.AcceptOrderAdapter
import com.svindo.vendorapp.adapters.ViewAddonsAdapter
import com.svindo.vendorapp.databinding.AcceptorderlayoutdesignBinding
import com.svindo.vendorapp.databinding.ActivityShipOrderScreenBinding
import com.svindo.vendorapp.modelclass.OrderAcceptModal
import com.svindo.vendorapp.modelclass.Verify_otp_Response
import com.svindo.vendorapp.modelclass.ViewAddonsListModal
import com.svindo.vendorapp.services.ApiClient
import com.svindo.vendorapp.services.ApiInterface
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var shiporderOrderBinding: ActivityShipOrderScreenBinding
class ShipOrderScreen : AppCompatActivity() {

    private lateinit var Binding: AcceptorderlayoutdesignBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var shiporderresponse: OrderAcceptModal
    private lateinit var orderstatusresponse: Verify_otp_Response
    private lateinit var addonsresponse: ViewAddonsListModal
    lateinit var adapter: AcceptOrderAdapter
    lateinit var adapter1: ViewAddonsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shiporderOrderBinding = ActivityShipOrderScreenBinding.inflate(layoutInflater)


        sharedPreference = SharedPreference(this)
        setContentView(shiporderOrderBinding.root)

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager1= LinearLayoutManager(this)

        shiporderOrderBinding.recyclerview.layoutManager = linearLayoutManager
        shiporderOrderBinding.recyclerview.hasFixedSize()

        shiporderOrderBinding.addonsrecyclerview.layoutManager = linearLayoutManager1
        shiporderOrderBinding.addonsrecyclerview.hasFixedSize()

        val loginButton = findViewById<ImageView>(R.id.shiporderbackbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        shiporderOrderBinding.shipbutton.setOnClickListener {
            val i = Intent(this@ShipOrderScreen,DeliveredScreen::class.java)
            startActivity(i)
        }

        Shiporder()
        OrderStatus()

    }
    fun Shiporder() {
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
                                shiporderresponse = response.body()!!
                                shiporderOrderBinding.otptext.text = shiporderresponse.order_details.otp
                                shiporderOrderBinding.name.text = shiporderresponse.order_details.user_name
                                shiporderOrderBinding.addresstxt.text = shiporderresponse.order_details.customer_address
                                shiporderOrderBinding.Orderdate.text = shiporderresponse.order_details.order_date
                                //shiporderOrderBinding.grandtotal.text = acceptorderresponse.order_details.net_payable_amount
                                shiporderOrderBinding.orderamount.text = shiporderresponse.order_details.order_amount
                                shiporderOrderBinding.orderstatus.text = shiporderresponse.order_details.order_status
                                shiporderOrderBinding.orderno.text = shiporderresponse.order_details.order_number

//                                OrderAddons(id:String)


                                if (shiporderresponse.list.isNotEmpty()) {
                                    shiporderOrderBinding.recyclerview.visibility = View.VISIBLE
                                    //  acceptOrderBinding.noData.visibility = View.GONE
                                    adapter = AcceptOrderAdapter(shiporderresponse.list, applicationContext)

                                    shiporderOrderBinding.recyclerview.adapter = adapter
                                    for (item in shiporderresponse.list) {
                                        // body of loop
                                        OrderAddons(item.id);
                                    }
                                } else {
                                    shiporderOrderBinding.recyclerview.visibility = View.GONE
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
                                    shiporderOrderBinding.addonsrecyclerview.visibility = View.VISIBLE
                                    //  acceptOrderBinding.noData.visibility = View.GONE
                                    adapter1 = ViewAddonsAdapter(addonsresponse.addon_list, applicationContext)

                                    shiporderOrderBinding.addonsrecyclerview.adapter = adapter1
                                } else {
                                    shiporderOrderBinding.addonsrecyclerview.visibility = View.GONE
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