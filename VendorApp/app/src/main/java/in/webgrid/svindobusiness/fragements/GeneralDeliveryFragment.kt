package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.graphics.Color
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
import`in`.webgrid.svindobusiness.databinding.GeneraldeliveryfragmentBinding
import`in`.webgrid.svindobusiness.modelclass.OrdersListModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var generalbinding: GeneraldeliveryfragmentBinding

class GeneralDeliveryFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var generalResponse: OrdersListModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: InstantAdapter

    var status=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(requireContext())
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        generalbinding = GeneraldeliveryfragmentBinding.inflate(inflater, container, false)

        generalbinding = GeneraldeliveryfragmentBinding.inflate(layoutInflater)

        linearLayoutManager = LinearLayoutManager(context)
        generalbinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        generalbinding.newordersRequestsViewRecyclerview.hasFixedSize()


        generalbinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground);
        generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background);
        generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background);
        generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background);



        generalbinding.Allbtn.setOnClickListener {
            generalbinding.Allbtn.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.Allbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "all"
            )
            generalbinding.Allbtn.setTextColor(Color.WHITE)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }

        Ordersdetails(
            status = "all"
        )

        generalbinding.Pendingbtn.setOnClickListener {
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.Pendingbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Order Placed"
            )

            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.WHITE)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }
        generalbinding.Acceptedbtn.setOnClickListener {

            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status = "Order Accepted"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.WHITE)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }


        generalbinding.pickupbtn.setOnClickListener {
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.button_loading_background)
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.pickupbtn.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Store Pickedup"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.WHITE)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }

        generalbinding.returnbtn.setOnClickListener {
            generalbinding.returnbtn.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.returnbtn.setBackgroundResource(R.drawable.buttonbackground)
            },2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Returned"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.WHITE)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }


        generalbinding.shopexchangebtn.setOnClickListener {
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.buttonbackground)
            },2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Exchanged"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.WHITE)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }


        generalbinding.cancelorders.setOnClickListener {
            generalbinding.cancelorders.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.cancelorders.setBackgroundResource(R.drawable.buttonbackground)
            },2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.delivered.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Cancelled"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.WHITE)
            generalbinding.delivered.setTextColor(Color.BLACK)
        }


        generalbinding.delivered.setOnClickListener {
            generalbinding.delivered.setBackgroundResource(R.drawable.button_loading_background);
            generalbinding.Allbtn.setEnabled(false)
            generalbinding.Pendingbtn.setEnabled(false)
            generalbinding.pickupbtn.setEnabled(false)
            generalbinding.Acceptedbtn.setEnabled(false)
            generalbinding.returnbtn.setEnabled(false)
            generalbinding.shopexchangebtn.setEnabled(false)
            generalbinding.cancelorders.setEnabled(false)
            generalbinding.delivered.setEnabled(false)

            Handler().postDelayed({
                generalbinding.Allbtn.setEnabled(true)
                generalbinding.Pendingbtn.setEnabled(true)
                generalbinding.pickupbtn.setEnabled(true)
                generalbinding.Acceptedbtn.setEnabled(true)
                generalbinding.returnbtn.setEnabled(true)
                generalbinding.shopexchangebtn.setEnabled(true)
                generalbinding.cancelorders.setEnabled(true)
                generalbinding.delivered.setEnabled(true)
                generalbinding.delivered.setBackgroundResource(R.drawable.buttonbackground)
            },2000)



            generalbinding.Allbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Acceptedbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.Pendingbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.pickupbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.returnbtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.shopexchangebtn.setBackgroundResource(R.drawable.pending_btn_background)
            generalbinding.cancelorders.setBackgroundResource(R.drawable.pending_btn_background)
            Ordersdetails(
                status="Delivered"
            )
            generalbinding.Allbtn.setTextColor(Color.BLACK)
            generalbinding.Pendingbtn.setTextColor(Color.BLACK)
            generalbinding.pickupbtn.setTextColor(Color.BLACK)
            generalbinding.Acceptedbtn.setTextColor(Color.BLACK)
            generalbinding.returnbtn.setTextColor(Color.BLACK)
            generalbinding.shopexchangebtn.setTextColor(Color.BLACK)
            generalbinding.cancelorders.setTextColor(Color.BLACK)
            generalbinding.delivered.setTextColor(Color.WHITE)
        }

        //   return inflater.inflate(R.layout.instantsfragment, container, false)
        return generalbinding.root
    }
    fun Ordersdetails(
        status:String,
    ) {
        try {
            generalbinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.OrdersDetails(sharedPreference.getValueString("token"),"Genral",status)
            requestCall.enqueue(object : Callback<OrdersListModal> {
                override fun onResponse(
                    call: Call<OrdersListModal>,
                    response: Response<OrdersListModal>
                ) {
                    generalbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                generalResponse = response.body()!!
                                // print(instantResponse)
                                if (generalResponse != null) {
                                    if (generalResponse.error == "0") {
                                        if (generalResponse.orders.isNotEmpty()) {

                                            generalbinding.newordersRequestsViewRecyclerview.visibility =
                                                View.VISIBLE
                                            adapter = context?.let {
                                                InstantAdapter(
                                                    generalResponse.orders,
                                                    context = it
                                                )
                                            }!!
                                            generalbinding.newordersRequestsViewRecyclerview.adapter =
                                                adapter

                                        } else {
                                            Toast.makeText(
                                                context,
                                                "List is Empty",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            generalbinding.newordersRequestsViewRecyclerview.visibility =
                                                View.GONE
                                            //generalbinding.noData.visibility = View.VISIBLE
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
                    generalbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}
