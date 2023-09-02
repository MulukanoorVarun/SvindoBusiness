package com.example.vendorapp.fragements

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.BannersAdapter
import com.example.vendorapp.adapters.DashboardAdapter
import com.example.vendorapp.databinding.FragmentHomeBinding
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
private lateinit var dashboardBinding: FragmentHomeBinding
class HomeFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var dashboardResponse: DashboardResponseModal
    lateinit var statusresponse: VendorStatusUpadateModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    private lateinit var adapter: DashboardAdapter
    private lateinit var banneradapter: BannersAdapter

    var shopstatus=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardBinding = FragmentHomeBinding.inflate(inflater, container, false)
        dashboardBinding = FragmentHomeBinding.inflate(layoutInflater)


        linearLayoutManager = LinearLayoutManager(context)
        dashboardBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        dashboardBinding.newordersRequestsViewRecyclerview.hasFixedSize()
       // dashboardBinding.newordersRequestsViewRecyclerview.setNestedScrollingEnabled(false)


        linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        dashboardBinding.horizontalrv.layoutManager = linearLayoutManager1
        dashboardBinding.horizontalrv.hasFixedSize()


//        dashboardBinding.shopstatusswitch.setOnClickListener() {
//                ShopStatusDetail(
//                    shop_status = shopstatus.toString().trim(),
//                )
//        }

        dashboarddetails()

        return dashboardBinding.root

        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home2, container, false)
    }
//    @Deprecated("Deprecated in Java")
//    @Override fun onBackPressed() {
//        val intent = Intent()
//        intent.putExtra("EXIT", true)
//        AlertDialog.Builder(requireContext())
//            .setTitle("Really Exit?")
//            .setMessage("Are you sure you want to exit?")
//            .setNegativeButton(android.R.string.no, null)
//            .setPositiveButton(
//                android.R.string.yes
//            )
//            { arg0, arg1 ->   requireActivity().setResult(Activity.RESULT_OK, intent)
////                val i = Intent(this@BusinessdetailsActivity, SplashActivity::class.java)
////                startActivity(i)
//                //moveTaskToBack(true);
//                exitProcess(-1)
//            }.create().show()
//    }



    fun dashboarddetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.Dashboarddetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<DashboardResponseModal> {
                override fun onResponse(
                    call: Call<DashboardResponseModal>,
                    response: Response<DashboardResponseModal>
                ) {
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when{
                            response.code() == 200 ->{

                                dashboardResponse = response.body()!!

                                    if(dashboardResponse !=null) {
                                        if (dashboardResponse.error == "0") {
                                            dashboardBinding.deliveredorderTxt.text =
                                                dashboardResponse.counts.delivered_order_count
                                            dashboardBinding.pendingorderTxt.text =
                                                dashboardResponse.counts.pending_order_count
                                            dashboardBinding.returnorderTxt.text =
                                                dashboardResponse.counts.return_order_count
                                            dashboardBinding.totalsaletxt.text =
                                                dashboardResponse.counts.totalsales_order_count
                                            dashboardBinding.textview1.text = dashboardResponse.counts.store_details.store_name

                                            dashboardBinding.shopstatusswitch.setOnClickListener() {
                                                if (dashboardResponse.counts.store_details.open_status == "Open") {
                                                    shopstatus = "Closed"
                                                    ShopStatusDetail(
                                                        shop_status = shopstatus.toString().trim(),
                                                    )
                                                } else if (dashboardResponse.counts.store_details.open_status == "Closed") {
                                                    shopstatus = "Open"
                                                    ShopStatusDetail(
                                                        shop_status = shopstatus.toString().trim(),
                                                    )
                                                }
                                            }
                                            if (dashboardResponse.counts.store_details.open_status == "Closed") {
                                                val disable = false
                                                dashboardBinding.shopstatusswitch.isChecked = disable
                                            } else {
                                                val enable = true
                                                dashboardBinding.shopstatusswitch.isChecked = enable
                                            }
                                            if (dashboardResponse.orders.isNotEmpty()) {
                                                dashboardBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                                dashboardBinding.noData.visibility = View.GONE
                                                adapter = context?.let { DashboardAdapter(dashboardResponse.orders, context = it) }!!
                                                dashboardBinding.newordersRequestsViewRecyclerview.adapter = adapter
                                            } else {
                                                dashboardBinding.newordersRequestsViewRecyclerview.visibility = View.GONE
                                                dashboardBinding.noData.visibility = View.VISIBLE
                                            }
                                            if (dashboardResponse.banners.isNotEmpty()) {
                                                dashboardBinding.horizontalrv.visibility = View.VISIBLE
                                                dashboardBinding.noData.visibility = View.GONE
                                                banneradapter = context?.let { BannersAdapter(dashboardResponse.banners, context = it) }!!
                                                dashboardBinding.horizontalrv.adapter = banneradapter
                                            } else {
                                                dashboardBinding.horizontalrv.visibility = View.GONE
                                                dashboardBinding.noData.visibility = View.VISIBLE
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
                    }catch (e: TimeoutException){
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DashboardResponseModal>, t: Throwable){
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e: Exception){
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun ShopStatusDetail(
        shop_status:String,
    ) {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.StatusDetails(sharedPreference.getValueString("token"),"shop_status","0",shop_status)
            requestCall.enqueue(object : Callback<VendorStatusUpadateModal> {
                override fun onResponse(
                    call: Call<VendorStatusUpadateModal>,
                    response: Response<VendorStatusUpadateModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when{
                            response.code() == 200 ->{
                                statusresponse = response.body()!!
                                if (statusresponse.error == "0") {
                                    if(statusresponse.status=="Closed"){
                                        dashboardResponse.counts.store_details.open_status="Closed"
                                        val disable=false
                                        dashboardBinding.shopstatusswitch.isChecked=disable
                                    }
                                    else {
                                        val enable=true
                                        dashboardResponse.counts.store_details.open_status=statusresponse.status
                                        dashboardBinding.shopstatusswitch.isChecked=enable
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
                    }catch (e: TimeoutException){
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<VendorStatusUpadateModal>, t: Throwable){
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e: Exception){
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    }



