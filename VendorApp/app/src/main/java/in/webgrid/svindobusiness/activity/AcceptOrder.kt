package `in`.webgrid.svindobusiness.activity
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.webgrid.svindobusiness.R


import `in`.webgrid.svindobusiness.databinding.ActivityAcceptOrderBinding
import `in`.webgrid.svindobusiness.modelclass.OrderAcceptModal


import `in`.webgrid.svindobusiness.services.ApiClient
import `in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import `in`.webgrid.svindobusiness.adapters.AcceptOrderAdapter
import `in`.webgrid.svindobusiness.adapters.ViewAddonsAdapter
import `in`.webgrid.svindobusiness.databinding.AcceptorderlayoutdesignBinding
import `in`.webgrid.svindobusiness.databinding.ActivityOtpBinding
import `in`.webgrid.svindobusiness.modelclass.Verify_otp_Response
import `in`.webgrid.svindobusiness.modelclass.ViewAddonsListModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var acceptOrderBinding: ActivityAcceptOrderBinding
class AcceptOrder : AppCompatActivity() {
    private lateinit var Binding: AcceptorderlayoutdesignBinding
    private lateinit var otpbinding: ActivityOtpBinding
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
    lateinit var progress: ProgressDialog

    var orderstatus="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        val order_id = intent.getStringExtra("order_id")
      //  showToast(order_id.toString())

        super.onCreate(savedInstanceState)
        acceptOrderBinding = ActivityAcceptOrderBinding.inflate(layoutInflater)

        Binding=AcceptorderlayoutdesignBinding.inflate(layoutInflater)
        otpbinding=ActivityOtpBinding.inflate(layoutInflater)

        sharedPreference = SharedPreference(this)
        setContentView(acceptOrderBinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(false)
        progress.setCancelable(false)
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

        Acceptorder(order_id!!)

    }
     fun showOTPDialog(order_id: String) {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
         val rootView = otpbinding.root
         // Check if the rootView already has a parent
         val parent = rootView.parent as? ViewGroup
         parent?.removeView(rootView)
        builder.setView(otpbinding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
         otpbinding.submit.setOnClickListener {
            if (otpbinding.firstPinView.text.toString().trim().isEmpty()) {
                showToast("OTP should be of minimum of 6 numbers")
                otpbinding.firstPinView.error = "Enter otp"
            } else if (otpbinding.firstPinView.text.toString().trim().length != 4) {
                showToast("OTP should be of minimum of 6 numbers")
            } else if (otpbinding.firstPinView.text.toString().trim().length == 4) {
                val otp: String = otpbinding.firstPinView.text.toString()
                Delivered_order(
                    order_id.toString().trim(),
                    otp = otp.toString().trim()
                )
            }
        }
    }

    internal fun Delivered_order(order_id: String, otp: String) {
        progress.show()
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.Order_delivered(
            sharedPreference.getValueString("token"),
            order_id,
            otp
        )
//        val requestCall = loginService.vehicaldetailsupdatedetails(SessionManager.getToken(),vehical_number, vehical_model)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            //if you receive http response then this method will executed
            //your status code decide if your http response is a success or failure
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {progress.dismiss()
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        orderstatusresponse = response.body()!!
                        if (orderstatusresponse.error == "0") {
                            progress.dismiss()
                            showToast(orderstatusresponse.message.toString())
                            val intent = Intent(this@AcceptOrder, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            alertDialog.hide()
                        }else{
                            showToast(orderstatusresponse.message.toString())
                            alertDialog.hide()
                        }
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
            override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                progress.dismiss()
                showToast(getString(R.string.session_exp))
            }
        })
    }
    fun Acceptorder(id: String) {
      progress.show()
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
                ) { progress.dismiss()
                    try {
                        when {
                            response.code() == 200 -> {
                                acceptorderresponse = response.body()!!
                                if (acceptorderresponse.error == "0") {
                                    progress.dismiss()
//                                    acceptOrderBinding.otptext.text =
//                                        acceptorderresponse.order_details.otp
                                    acceptOrderBinding.name.text =
                                        acceptorderresponse.order_details.user_name
                                    acceptOrderBinding.addressTxt.text =
                                        acceptorderresponse.order_details.customer_address
                                    acceptOrderBinding.Orderdate.text =
                                        acceptorderresponse.order_details.order_date
                                    acceptOrderBinding.grandtotal.text =
                                        acceptorderresponse.order_details.net_payable_amount
                                    acceptOrderBinding.orderamount.text = acceptorderresponse.order_details.order_amount
                                    acceptOrderBinding.orderstatus.text =
                                        acceptorderresponse.order_details.order_status
                                    acceptOrderBinding.orderno.text = acceptorderresponse.order_details.order_number
                                    acceptOrderBinding.paymentmode.text = acceptorderresponse.order_details.payment_type
                                    acceptOrderBinding.subsidyamt.text = acceptorderresponse.order_details.delivery_discount
                                    acceptOrderBinding.deliveryfee.text = acceptorderresponse.order_details.porter_delivery_fee
                                    acceptOrderBinding.itemtotal.text = acceptorderresponse.order_details.order_amount
                                    acceptOrderBinding.deliverytype.text = acceptorderresponse.order_details.delivery_type
                                    acceptOrderBinding.commentsDesc.text = acceptorderresponse.order_details.comment


                                    val phoneNumber = acceptorderresponse.order_details.customer_mobile_number?.trim()
                                    acceptOrderBinding.calltocustomer.setOnClickListener {
                                        if (!phoneNumber.isNullOrEmpty()) {
                                            val intent = Intent(Intent.ACTION_DIAL)
                                            intent.data = Uri.parse("tel:$phoneNumber")
                                            startActivity(intent)
                                        } else {
                                            Log.d("DialPad", "Phone number is empty or null")
                                        }
                                    }

                                    if(acceptorderresponse.order_details.delivery_type=="Genral") {
                                        acceptOrderBinding.deliverytype.text = "General"
                                    }else{
                                        acceptOrderBinding.deliverytype.text = acceptorderresponse.order_details.delivery_type
                                    }


                                    var availability = acceptorderresponse.order_details.is_delivery_boy_available
                                    if (availability == "0") {
                                        acceptOrderBinding.Cardview1.isVisible = false
                                    } else {
                                        acceptOrderBinding.Cardview1.isVisible = true
                                    }

                                    var order_id = acceptorderresponse.order_details.id
                                    orderstatus = acceptorderresponse.order_details.order_status

                                    var delivery_type=acceptorderresponse.order_details.delivery_type

                                    if ((orderstatus == "Store Pickedup" && delivery_type=="Self Pickup")){
                                        acceptOrderBinding.acceptbutton.isVisible = true
                                        acceptOrderBinding.rejectbutton.isVisible = false
                                        acceptOrderBinding.linearlayout7.isVisible = true
                                        acceptOrderBinding.commentsLayout.isVisible = false
                                    }else if(orderstatus != "Order Placed" && orderstatus != "Order Accepted"){
                                        acceptOrderBinding.acceptbutton.isVisible = false
                                        acceptOrderBinding.rejectbutton.isVisible = false
                                        if(orderstatus == "Exchange Requested" || orderstatus == "Exchange Accepted"){
                                            acceptOrderBinding.linearlayout7.isVisible =false
                                            acceptOrderBinding.commentsLayout.isVisible = true
                                            acceptOrderBinding.acceptbutton.isVisible = true
                                            acceptOrderBinding.rejectbutton.isVisible = false
                                        }else if(orderstatus == "Exchanged"){
                                                acceptOrderBinding.linearlayout7.isVisible = false
                                                acceptOrderBinding.commentsLayout.isVisible = true
                                        }else{
                                            acceptOrderBinding.linearlayout7.isVisible = true
                                            acceptOrderBinding.commentsLayout.isVisible = false
                                        }

                                    }else{
                                        acceptOrderBinding.linearlayout7.isVisible = true
                                        acceptOrderBinding.commentsLayout.isVisible = false
                                    }

                                    if (orderstatus == "Order Accepted") {
                                        acceptOrderBinding.acceptbutton.text = "Ship Order"
                                    }else if(orderstatus == "Store Pickedup"){
                                        acceptOrderBinding.acceptbutton.text = "Delivered Order"
                                    }else if(orderstatus == "Exchange Accepted"){
                                        acceptOrderBinding.acceptbutton.text = "Exchanged Order"
                                    }

                                    if(acceptorderresponse.order_details.payment_type!="Pay to Shop") {
                                        acceptOrderBinding.billdownloadbtn.isVisible=true
                                        acceptOrderBinding.calltocustomer.isVisible=false
                                        acceptOrderBinding.billdownloadbtn.setOnClickListener {
                                            var url = acceptorderresponse.bills
                                            val browserIntent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            startActivity(browserIntent)
                                        }
                                    }else{
                                        acceptOrderBinding.billdownloadbtn.isVisible=false
                                        acceptOrderBinding.calltocustomer.isVisible=true
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
                                        if (orderstatus == "Order Placed"){
                                            orderstatus = "Order Accepted"
//                                        showToast(orderstatus.toString())
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        } else if (orderstatus == "Order Accepted") {
                                            acceptOrderBinding.acceptbutton.text = "Ship Order"
                                            orderstatus = "Store Pickedup"
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        } else if(orderstatus == "Exchange Requested"){
                                            orderstatus = "Exchange Accepted"
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        } else if(orderstatus == "Exchange Accepted"){
                                            orderstatus = "Exchanged"
                                            OrderStatus(
                                                orderstatus = orderstatus.toString(),
                                                order_id = order_id.toString()
                                            )
                                        } else{
                                            showOTPDialog(order_id)
                                        }
                                    }
                                    if (acceptorderresponse.list.isNotEmpty()) {
                                        acceptOrderBinding.acceptorderrecyclerview.visibility = View.VISIBLE
                                        adapter = AcceptOrderAdapter(acceptorderresponse.list, applicationContext)
                                        acceptOrderBinding.acceptorderrecyclerview.adapter = adapter
                                        for (item in acceptorderresponse.list) {
                                            OrderAddons(item.id)
                                        }
                                    } else {
                                        acceptOrderBinding.acceptorderrecyclerview.visibility = View.GONE
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
                    progress.dismiss()
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }
    fun OrderStatus(
        orderstatus:String,
        order_id:String) {
        progress.show()
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
                    progress.dismiss()
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                orderstatusresponse = response.body()!!
                                if (orderstatusresponse.error == "0") {
                                    progress.dismiss()
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
                    progress.dismiss()
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
                                  //  showToast("Svindo")
                                    if(addonsresponse.addon_list.isNotEmpty() && addonsresponse.addon_list!=null){
                                    //    showToast("hi")
                                        acceptOrderBinding.addonsrecyclerview.visibility = View.VISIBLE
                                        adapter1 = ViewAddonsAdapter(addonsresponse.addon_list, applicationContext)
                                        acceptOrderBinding.addonsrecyclerview.adapter = adapter1
                                    } else {
                                      //  showToast("varun")
                                        acceptOrderBinding.addonsrecyclerview.visibility = View.GONE
                                        acceptOrderBinding.Cardview.isVisible = false
                                    }
                                }else{
                                  //  showToast("Svindo Business")
                                    acceptOrderBinding.Cardview.isVisible = false
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