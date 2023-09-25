package com.svindo.vendorapp.activity
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.svindo.vendorapp.R
import com.svindo.vendorapp.adapters.AcceptOrderAdapter
import com.svindo.vendorapp.adapters.ViewAddonsAdapter
import com.svindo.vendorapp.databinding.AcceptorderlayoutdesignBinding
import com.svindo.vendorapp.databinding.ActivityAcceptOrderBinding
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
private lateinit var acceptOrderBinding: ActivityAcceptOrderBinding
class AcceptOrder : AppCompatActivity() {
    private lateinit var Binding: AcceptorderlayoutdesignBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var acceptorderresponse: OrderAcceptModal
    private lateinit var orderstatusresponse: Verify_otp_Response
    private lateinit var addonsresponse: ViewAddonsListModal
    lateinit var adapter: AcceptOrderAdapter
    lateinit var adapter1: ViewAddonsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    var orderstatus="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        val order_id = intent.getStringExtra("order_id")
      //  showToast(order_id.toString())

        super.onCreate(savedInstanceState)
        acceptOrderBinding = ActivityAcceptOrderBinding.inflate(layoutInflater)

        Binding=AcceptorderlayoutdesignBinding.inflate(layoutInflater)

        sharedPreference = SharedPreference(this)
        setContentView(acceptOrderBinding.root)
       // setContentView(Binding.root)

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager1= LinearLayoutManager(this)

        acceptOrderBinding.acceptorderrecyclerview.layoutManager = linearLayoutManager
        acceptOrderBinding.acceptorderrecyclerview.hasFixedSize()

        acceptOrderBinding.addonsrecyclerview.layoutManager = linearLayoutManager1
        acceptOrderBinding.addonsrecyclerview.hasFixedSize()

        val loginButton = findViewById<ImageView>(R.id.acceptbackbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

//        acceptOrderBinding.acceptbutton.setOnClickListener {
//            val i = Intent(this@AcceptOrder,ShipOrderScreen::class.java)
//            startActivity(i)
//        }

        Acceptorder(order_id!!)

    }
//    internal fun showAlertDialog() {
//        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
//        val binding = ViewaddonslayoutBinding.inflate(layoutInflater)
//        builder.setView(binding.root)
//        builder.setCancelable(true)
//        alertDialog = builder.create()
//        alertDialog.show()
//        alertDialog.setCanceledOnTouchOutside(false)
//    }
    fun Acceptorder(id: String) {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AcceptOrdersDetails(sharedPreference.getValueString("token"),id)
            requestCall.enqueue(object : Callback<OrderAcceptModal> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<OrderAcceptModal>,
                    response: Response<OrderAcceptModal>
                ) {
                    try {
                        when {
                            response.code() == 200 -> {
                                acceptorderresponse = response.body()!!
                                if (acceptorderresponse.error == "0") {
                                    acceptOrderBinding.otptext.text =
                                        acceptorderresponse.order_details.otp
                                    acceptOrderBinding.name.text =
                                        acceptorderresponse.order_details.user_name
                                    acceptOrderBinding.addressTxt.text =
                                        acceptorderresponse.order_details.customer_address
                                    acceptOrderBinding.Orderdate.text =
                                        acceptorderresponse.order_details.order_date
                                    acceptOrderBinding.grandtotal.text =
                                        acceptorderresponse.order_details.net_payable_amount
                                    acceptOrderBinding.orderamount.text =
                                        acceptorderresponse.order_details.order_amount
                                    acceptOrderBinding.orderstatus.text =
                                        acceptorderresponse.order_details.order_status
                                    acceptOrderBinding.orderno.text =
                                        acceptorderresponse.order_details.order_number
                                    acceptOrderBinding.paymentmode.text =
                                        acceptorderresponse.order_details.payment_type
                                    acceptOrderBinding.subsidyamt.text =
                                        acceptorderresponse.order_details.delivery_discount
                                    acceptOrderBinding.deliveryfee.text =
                                        acceptorderresponse.order_details.porter_delivery_fee
                                    acceptOrderBinding.itemtotal.text = acceptorderresponse.order_details.order_amount
                                    acceptOrderBinding.deliverytype.text = acceptorderresponse.order_details.delivery_type


                                    var availability =
                                        acceptorderresponse.order_details.is_delivery_boy_available
                                    if (availability == "0") {
                                        acceptOrderBinding.Cardview1.isVisible = false;
                                    } else {
                                        acceptOrderBinding.Cardview1.isVisible = true;
                                    }
//                                OrderStatus(
//                                    orderstatus=orderstatus.toString(),
//                                    order_id=acceptorderresponse.order_details.id.toString())
                                    var order_id = acceptorderresponse.order_details.id
                                    orderstatus = acceptorderresponse.order_details.order_status

                                    if (orderstatus != "Order Placed" && orderstatus != "Order Accepted") {
                                        acceptOrderBinding.acceptbutton.isVisible = false;
                                        acceptOrderBinding.rejectbutton.isVisible = false;

                                    }
                                    if (orderstatus == "Order Accepted") {
                                        acceptOrderBinding.acceptbutton.text = "Ship Order"
                                    }

                                    acceptOrderBinding.rejectbutton.setOnClickListener {
                                        if (orderstatus == "Order Placed") {
                                            orderstatus = "Store Rejected"
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        }
                                    }

                                    acceptOrderBinding.acceptbutton.setOnClickListener {
                                        if (orderstatus == "Order Placed") {
                                            orderstatus = "Order Accepted"
//                                        showToast(orderstatus.toString())
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        } else if (orderstatus == "Order Accepted") {

                                            acceptOrderBinding.acceptbutton.text = "Ship Order"

                                            orderstatus = "Store Pickedup"
//                                        showToast(orderstatus.toString())
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        }
                                    }
                                    if (acceptorderresponse.list.isNotEmpty()) {
                                        acceptOrderBinding.acceptorderrecyclerview.visibility =
                                            View.VISIBLE
                                        //  acceptOrderBinding.noData.visibility = View.GONE
                                        adapter = AcceptOrderAdapter(
                                            acceptorderresponse.list,
                                            applicationContext
                                        )

                                        acceptOrderBinding.acceptorderrecyclerview.adapter = adapter
                                        for (item in acceptorderresponse.list) {
                                            // body of loop
                                            OrderAddons(item.id);
                                        }
                                    } else {
                                        acceptOrderBinding.acceptorderrecyclerview.visibility = View.GONE
                                        // acceptOrderBinding.noData.visibility = View.VISIBLE

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
    fun OrderStatus(orderstatus:String,
                    order_id:String) {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.OrderStatusDeatils(sharedPreference.getValueString("token"),order_id,orderstatus)
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
                                if (orderstatusresponse.error == "0") {
                                    showToast(orderstatusresponse.message.toString())
                                    Acceptorder(order_id)
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
                                if (addonsresponse.error=="0")
                                {
                                    if (addonsresponse.addon_list.isNotEmpty()) {
                                        acceptOrderBinding.addonsrecyclerview.visibility = View.VISIBLE
                                        //  acceptOrderBinding.noData.visibility = View.GONE
                                        adapter1 = ViewAddonsAdapter(addonsresponse.addon_list, applicationContext)

                                        acceptOrderBinding.addonsrecyclerview.adapter = adapter1
                                    } else {
                                        acceptOrderBinding.addonsrecyclerview.visibility = View.GONE
                                        // acceptOrderBinding.noData.visibility = View.VISIBLE

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