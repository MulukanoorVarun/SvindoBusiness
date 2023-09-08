package com.example.vendorapp.fragements

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.InstantAdapter
import com.example.vendorapp.databinding.InstantsfragmentBinding
import com.example.vendorapp.modelclass.OrdersListModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var instantBinding: InstantsfragmentBinding

class InstantsFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var instantResponse: OrdersListModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: InstantAdapter


    var status_glb=""
    var status=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference=SharedPreference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        // Inflate the layout for this fragment
        instantBinding = InstantsfragmentBinding.inflate(inflater, container, false)
        instantBinding = InstantsfragmentBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        instantBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        instantBinding.newordersRequestsViewRecyclerview.hasFixedSize()

        instantBinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground)
        instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
        instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
        instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)


        instantBinding.Allbtn.setOnClickListener{
            instantBinding.Allbtn.setBackgroundResource(R.drawable.button_loading_background)
            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)

            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground)
               // instantBinding.Allbtn.setTextColor(Color.WHITE)
            }, 2000)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "all"
            )
            instantBinding.Allbtn.setTextColor(Color.WHITE)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
        }


                instantBinding.Pendingbtn.setOnClickListener {
                    instantBinding.Pendingbtn.setBackgroundResource(R.drawable.button_loading_background)
                    instantBinding.Allbtn.setEnabled(false)
                    instantBinding.Pendingbtn.setEnabled(false)
                    instantBinding.pickupbtn.setEnabled(false)
                    instantBinding.Acceptedbtn.setEnabled(false)
                    instantBinding.returnbtn.setEnabled(false)
                    instantBinding.shopexchangebtn.setEnabled(false)
                    Handler().postDelayed({
                        instantBinding.Allbtn.setEnabled(true)
                        instantBinding.Pendingbtn.setEnabled(true)
                        instantBinding.pickupbtn.setEnabled(true)
                        instantBinding.Acceptedbtn.setEnabled(true)
                        instantBinding.returnbtn.setEnabled(true)
                        instantBinding.shopexchangebtn.setEnabled(true)
                        instantBinding.Pendingbtn.setBackgroundResource(R.drawable.buttonbackground)
                      //  instantBinding.Pendingbtn.setTextColor(Color.WHITE)
                    }, 2000)
                    instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Pending"
            )
                    instantBinding.Allbtn.setTextColor(Color.BLACK)
                    instantBinding.Pendingbtn.setTextColor(Color.WHITE)
                    instantBinding.pickupbtn.setTextColor(Color.BLACK)
                    instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
                    instantBinding.returnbtn.setTextColor(Color.BLACK)
                    instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
        }


        instantBinding.Acceptedbtn.setOnClickListener {
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.button_loading_background);
            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)

            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Accepted"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.WHITE)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
        }


            instantBinding.pickupbtn.setOnClickListener {
                instantBinding.pickupbtn.setBackgroundResource(R.drawable.button_loading_background)

                instantBinding.Allbtn.setEnabled(false)
                instantBinding.Pendingbtn.setEnabled(false)
                instantBinding.pickupbtn.setEnabled(false)
                instantBinding.Acceptedbtn.setEnabled(false)
                instantBinding.returnbtn.setEnabled(false)
                instantBinding.shopexchangebtn.setEnabled(false)


                Handler().postDelayed({
                    instantBinding.Allbtn.setEnabled(true)
                    instantBinding.Pendingbtn.setEnabled(true)
                    instantBinding.pickupbtn.setEnabled(true)
                    instantBinding.Acceptedbtn.setEnabled(true)
                    instantBinding.returnbtn.setEnabled(true)
                    instantBinding.shopexchangebtn.setEnabled(true)
                   instantBinding.pickupbtn.setBackgroundResource(R.drawable.buttonbackground)
                }, 2000)

                    instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
                Ordersdetails(
                    status="Delivered"
                )
                instantBinding.Allbtn.setTextColor(Color.BLACK)
                instantBinding.Pendingbtn.setTextColor(Color.BLACK)
                instantBinding.pickupbtn.setTextColor(Color.WHITE)
                instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
                instantBinding.returnbtn.setTextColor(Color.BLACK)
                instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
        }


        instantBinding.returnbtn.setOnClickListener {

            instantBinding.returnbtn.setBackgroundResource(R.drawable.button_loading_background)

            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)


            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.returnbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Return"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.WHITE)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
        }


        instantBinding.shopexchangebtn.setOnClickListener {

            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.button_loading_background)

            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="ShopExchange"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.WHITE)
        }
        return instantBinding.root
    }

//    fun getPackageName(): String?{
//        return context!!.packageName
//    }


    fun Ordersdetails(
        status:String,
    ) {
        try {

            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.OrdersDetails(sharedPreference.getValueString("token"),"Instant",status)
            requestCall.enqueue(object : Callback<OrdersListModal> {
                override fun onResponse(
                    call: Call<OrdersListModal>,
                    response: Response<OrdersListModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                instantResponse = response.body()!!
                               // print(instantResponse)
                                if (instantResponse != null) {
                                    if (instantResponse.error == "0") {
                                        if (instantResponse.orders.isNotEmpty()) {
                                            instantBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            adapter = context?.let {
                                                InstantAdapter(instantResponse.orders, context = it) }!!
                                            instantBinding.newordersRequestsViewRecyclerview.adapter = adapter
                                        } else {
                                    Toast.makeText(context,"List is Empty",Toast.LENGTH_SHORT).show()
                                    instantBinding.newordersRequestsViewRecyclerview.visibility = View.GONE
                                        }
                                    }
                                }

                            }

                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp),Toast.LENGTH_SHORT).show()

                            }else -> {
                                Toast.makeText(context,getString(R.string.server_error),Toast.LENGTH_SHORT).show()
                            }
                        }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrdersListModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }
}