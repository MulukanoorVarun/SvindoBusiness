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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.InstantAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentSelfPickupBinding
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
private lateinit var selfPickupBinding: FragmentSelfPickupBinding

class SelfPickupFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var instantResponse: OrdersListModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: InstantAdapter

    var status=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreference= SharedPreference(requireContext())
        selfPickupBinding = FragmentSelfPickupBinding.inflate(inflater, container, false)
        selfPickupBinding = FragmentSelfPickupBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        selfPickupBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        selfPickupBinding.newordersRequestsViewRecyclerview.hasFixedSize()

        if (checkForInternet(requireContext())) {
            // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        selfPickupBinding.Allbtn.setOnClickListener {
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.button_loading_background);
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "all"
            )

            selfPickupBinding.Allbtn.setTextColor(Color.WHITE)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)

        }

        Ordersdetails(
            status = "all"
        )

        selfPickupBinding.Pendingbtn.setOnClickListener {
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.button_loading_background);
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Order Placed"
            )

            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.WHITE)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }
        selfPickupBinding.Acceptedbtn.setOnClickListener {
            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.button_loading_background);
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Order Accepted"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.WHITE)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }


        selfPickupBinding.pickupbtn.setOnClickListener {
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.button_loading_background);
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Store Pickedup"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.WHITE)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }


        selfPickupBinding.returnbtn.setOnClickListener {
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.button_loading_background);
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Returned"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.WHITE)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }

        selfPickupBinding.shopexchange.setOnClickListener {
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.button_loading_background)
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Exchanged"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.WHITE)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }


        selfPickupBinding.cancelorders.setOnClickListener {
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.button_loading_background)
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Cancelled"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.WHITE)
            selfPickupBinding.delivered.setTextColor(Color.BLACK)
        }


        selfPickupBinding.delivered.setOnClickListener {
            selfPickupBinding.delivered.setBackgroundResource(R.drawable.button_loading_background)
            selfPickupBinding.Allbtn.setEnabled(false)
            selfPickupBinding.Pendingbtn.setEnabled(false)
            selfPickupBinding.pickupbtn.setEnabled(false)
            selfPickupBinding.Acceptedbtn.setEnabled(false)
            selfPickupBinding.returnbtn.setEnabled(false)
            selfPickupBinding.shopexchange.setEnabled(false)
            selfPickupBinding.cancelorders.setEnabled(false)
            selfPickupBinding.delivered.setEnabled(false)

            Handler().postDelayed({
                selfPickupBinding.Allbtn.setEnabled(true)
                selfPickupBinding.Pendingbtn.setEnabled(true)
                selfPickupBinding.pickupbtn.setEnabled(true)
                selfPickupBinding.Acceptedbtn.setEnabled(true)
                selfPickupBinding.returnbtn.setEnabled(true)
                selfPickupBinding.shopexchange.setEnabled(true)
                selfPickupBinding.cancelorders.setEnabled(true)
                selfPickupBinding.delivered.setEnabled(true)
                selfPickupBinding.delivered.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

            selfPickupBinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.shopexchange.setBackgroundResource(R.drawable.pending_btn_background)
            selfPickupBinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Delivered"
            )
            selfPickupBinding.Allbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Pendingbtn.setTextColor(Color.BLACK)
            selfPickupBinding.pickupbtn.setTextColor(Color.BLACK)
            selfPickupBinding.Acceptedbtn.setTextColor(Color.BLACK)
            selfPickupBinding.returnbtn.setTextColor(Color.BLACK)
            selfPickupBinding.shopexchange.setTextColor(Color.BLACK)
            selfPickupBinding.cancelorders.setTextColor(Color.BLACK)
            selfPickupBinding.delivered.setTextColor(Color.WHITE)
        }


        //   return inflater.inflate(R.layout.instantsfragment, container, false)
        return selfPickupBinding.root
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
    fun Ordersdetails(
        status:String,
    ) {
        try {
            selfPickupBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.OrdersDetails(sharedPreference.getValueString("token"),"Self Pickup",status)
            requestCall.enqueue(object : Callback<OrdersListModal> {
                override fun onResponse(
                    call: Call<OrdersListModal>,
                    response: Response<OrdersListModal>
                ) {
                    selfPickupBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                          instantResponse = response.body()!!
                                    if (instantResponse.error == "0") {
                                        if (instantResponse.orders.isNotEmpty()) {
                                            selfPickupBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            adapter = context?.let { InstantAdapter(instantResponse.orders,context = it) }!!
                                            selfPickupBinding.newordersRequestsViewRecyclerview.adapter =
                                                adapter

                                        } else {
                                            Toast.makeText(
                                                context,
                                                "List is Empty",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            selfPickupBinding.newordersRequestsViewRecyclerview.visibility =
                                                View.GONE
                                        }
                                    }
                                }

                            }

                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()

                            }

                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrdersListModal>, t: Throwable) {
                    selfPickupBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}

