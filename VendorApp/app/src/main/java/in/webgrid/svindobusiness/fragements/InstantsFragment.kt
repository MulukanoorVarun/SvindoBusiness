package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.InstantAdapter
import`in`.webgrid.svindobusiness.databinding.InstantsfragmentBinding
import`in`.webgrid.svindobusiness.modelclass.OrdersListModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.activity.NetworkIssueActivity
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
        sharedPreference= SharedPreference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        // Inflate the layout for this fragment
        instantBinding = InstantsfragmentBinding.inflate(inflater, container, false)
        instantBinding = InstantsfragmentBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        instantBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        instantBinding.newordersRequestsViewRecyclerview.hasFixedSize()


        if (checkForInternet(requireContext())) {
            // Toast.makeText(context, "You're Online !", Toast.LENGTH_SHORT).show()
        } else {
              //Toast.makeText(context, "You're Offline !, Check your network connection.", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }

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
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground)
               // instantBinding.Allbtn.setTextColor(Color.WHITE)
            }, 2000)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "all"
            )
            instantBinding.Allbtn.setTextColor(Color.WHITE)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
            instantBinding.cancelorders.setTextColor(Color.BLACK)
            instantBinding.delivered.setTextColor(Color.BLACK)
        }

        Ordersdetails(
            status = "all"
        )
                instantBinding.Pendingbtn.setOnClickListener {
                    instantBinding.Pendingbtn.setBackgroundResource(R.drawable.button_loading_background)
                    instantBinding.Allbtn.setEnabled(false)
                    instantBinding.Pendingbtn.setEnabled(false)
                    instantBinding.pickupbtn.setEnabled(false)
                    instantBinding.Acceptedbtn.setEnabled(false)
                    instantBinding.returnbtn.setEnabled(false)
                    instantBinding.shopexchangebtn.setEnabled(false)
                    instantBinding.cancelorders.setEnabled(false)
                    instantBinding.delivered.setEnabled(false)
                    Handler().postDelayed({
                        instantBinding.Allbtn.setEnabled(true)
                        instantBinding.Pendingbtn.setEnabled(true)
                        instantBinding.pickupbtn.setEnabled(true)
                        instantBinding.Acceptedbtn.setEnabled(true)
                        instantBinding.returnbtn.setEnabled(true)
                        instantBinding.shopexchangebtn.setEnabled(true)
                        instantBinding.cancelorders.setEnabled(true)
                        instantBinding.delivered.setEnabled(true)
                        instantBinding.Pendingbtn.setBackgroundResource(R.drawable.buttonbackground)
                      //  instantBinding.Pendingbtn.setTextColor(Color.WHITE)
                    }, 2000)
                    instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Order Placed"
            )
                    instantBinding.Allbtn.setTextColor(Color.BLACK)
                    instantBinding.Pendingbtn.setTextColor(Color.WHITE)
                    instantBinding.pickupbtn.setTextColor(Color.BLACK)
                    instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
                    instantBinding.returnbtn.setTextColor(Color.BLACK)
                    instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
                    instantBinding.cancelorders.setTextColor(Color.BLACK)
                    instantBinding.delivered.setTextColor(Color.BLACK)
        }


        instantBinding.Acceptedbtn.setOnClickListener {
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.button_loading_background);
            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Order Accepted"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.WHITE)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
            instantBinding.cancelorders.setTextColor(Color.BLACK)
            instantBinding.delivered.setTextColor(Color.BLACK)
        }


            instantBinding.pickupbtn.setOnClickListener {
                instantBinding.pickupbtn.setBackgroundResource(R.drawable.button_loading_background)

                instantBinding.Allbtn.setEnabled(false)
                instantBinding.Pendingbtn.setEnabled(false)
                instantBinding.pickupbtn.setEnabled(false)
                instantBinding.Acceptedbtn.setEnabled(false)
                instantBinding.returnbtn.setEnabled(false)
                instantBinding.shopexchangebtn.setEnabled(false)
                instantBinding.cancelorders.setEnabled(false)
                instantBinding.delivered.setEnabled(false)


                Handler().postDelayed({
                    instantBinding.Allbtn.setEnabled(true)
                    instantBinding.Pendingbtn.setEnabled(true)
                    instantBinding.pickupbtn.setEnabled(true)
                    instantBinding.Acceptedbtn.setEnabled(true)
                    instantBinding.returnbtn.setEnabled(true)
                    instantBinding.shopexchangebtn.setEnabled(true)
                    instantBinding.cancelorders.setEnabled(true)
                    instantBinding.delivered.setEnabled(true)
                   instantBinding.pickupbtn.setBackgroundResource(R.drawable.buttonbackground)
                }, 2000)

                    instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
                    instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
                Ordersdetails(
                    status="Store Pickedup"
                )
                instantBinding.Allbtn.setTextColor(Color.BLACK)
                instantBinding.Pendingbtn.setTextColor(Color.BLACK)
                instantBinding.pickupbtn.setTextColor(Color.WHITE)
                instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
                instantBinding.returnbtn.setTextColor(Color.BLACK)
                instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
                instantBinding.cancelorders.setTextColor(Color.BLACK)
                instantBinding.delivered.setTextColor(Color.BLACK)
        }


        instantBinding.returnbtn.setOnClickListener {
            instantBinding.returnbtn.setBackgroundResource(R.drawable.button_loading_background)
            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)


            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.returnbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Returned"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.WHITE)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
            instantBinding.cancelorders.setTextColor(Color.BLACK)
            instantBinding.delivered.setTextColor(Color.BLACK)
        }


        instantBinding.shopexchangebtn.setOnClickListener {

            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.button_loading_background)

            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)
            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Exchanged"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.WHITE)
            instantBinding.cancelorders.setTextColor(Color.BLACK)
            instantBinding.delivered.setTextColor(Color.BLACK)
        }

        instantBinding.cancelorders.setOnClickListener {

            instantBinding.cancelorders.setBackgroundResource(R.drawable.button_loading_background)

            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)
            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.cancelorders.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Cancelled"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
            instantBinding.cancelorders.setTextColor(Color.WHITE)
            instantBinding.delivered.setTextColor(Color.BLACK)
        }

        instantBinding.delivered.setOnClickListener {
            instantBinding.delivered.setBackgroundResource(R.drawable.button_loading_background)

            instantBinding.Allbtn.setEnabled(false)
            instantBinding.Pendingbtn.setEnabled(false)
            instantBinding.pickupbtn.setEnabled(false)
            instantBinding.Acceptedbtn.setEnabled(false)
            instantBinding.returnbtn.setEnabled(false)
            instantBinding.shopexchangebtn.setEnabled(false)
            instantBinding.cancelorders.setEnabled(false)
            instantBinding.delivered.setEnabled(false)
            Handler().postDelayed({
                instantBinding.Allbtn.setEnabled(true)
                instantBinding.Pendingbtn.setEnabled(true)
                instantBinding.pickupbtn.setEnabled(true)
                instantBinding.Acceptedbtn.setEnabled(true)
                instantBinding.returnbtn.setEnabled(true)
                instantBinding.shopexchangebtn.setEnabled(true)
                instantBinding.cancelorders.setEnabled(true)
                instantBinding.delivered.setEnabled(true)
                instantBinding.delivered.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            instantBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            instantBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Delivered"
            )
            instantBinding.Allbtn.setTextColor(Color.BLACK)
            instantBinding.Pendingbtn.setTextColor(Color.BLACK)
            instantBinding.pickupbtn.setTextColor(Color.BLACK)
            instantBinding.Acceptedbtn.setTextColor(Color.BLACK)
            instantBinding.returnbtn.setTextColor(Color.BLACK)
            instantBinding.shopexchangebtn.setTextColor(Color.BLACK)
            instantBinding.cancelorders.setTextColor(Color.BLACK)
            instantBinding.delivered.setTextColor(Color.WHITE)
        }
        return instantBinding.root
    }



    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

//    fun getPackageName(): String?{
//        return context!!.packageName
//    }

    fun Ordersdetails(status:String) {
        try {
            instantBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.OrdersDetails(sharedPreference.getValueString("token"),"Instant",status)
            requestCall.enqueue(object : Callback<OrdersListModal> {
                override fun onResponse(
                    call: Call<OrdersListModal>,
                    response: Response<OrdersListModal>
                ) {
                    instantBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                        instantResponse = response.body()!!
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
                    instantBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }
}