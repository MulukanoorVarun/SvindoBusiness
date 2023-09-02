package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.DashboardAdapter
import com.example.vendorapp.databinding.ActivityDashboardBinding
import com.example.vendorapp.modelclass.DashboardResponse
import com.example.vendorapp.modelclass.DashboardResponseModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("StaticFieldLeak")
private lateinit var dashboardBinding: ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    lateinit var dashboardResponse: DashboardResponseModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: DashboardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
       setContentView(dashboardBinding.root)
       //get_update_status()
       dashboardBinding.bottomnavigation.setOnNavigationItemSelectedListener { menuItem ->
           when (menuItem.itemId) {
               R.id.home -> {
                   true
               }
               R.id.orders -> {
                   val intent = Intent(this, OrdersScreen::class.java)
                   overridePendingTransition(0, 0);
                   intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   startActivity(intent)
                   false
               }
               R.id.catalogue -> {
                   val intent = Intent(this, catalogueActivity::class.java)
                   overridePendingTransition(0, 0);
                   intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   startActivity(intent)
                   false
               }
               R.id.account -> {
                   val intent = Intent(this, ProfileSrceenActivity::class.java)
                   overridePendingTransition(0, 0);
                   intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   startActivity(intent)
                   false
               }

               else -> false
           }
       }

       linearLayoutManager = LinearLayoutManager(this)
       dashboardBinding.newOrdersRequestsViewRecyclerview.layoutManager = linearLayoutManager
       dashboardBinding.newOrdersRequestsViewRecyclerview.hasFixedSize()
       dashboarddetails()

        dashboardBinding.switch1.setOnClickListener{
          //  update_status()

//            dashboardbinding.switch2.
        }
   }
       fun dashboarddetails() {
           try {
              // dashboardBinding.progressBarLay.visibility = View.VISIBLE
               val ordersService = ApiClient.buildService(ApiInterface::class.java)
               val requestCall = ordersService.Dashboarddetails("8aa65e3657407c1819df1d7c298eba633a3f9a0c710228571ef90f51eee2353508cabcfd8c1c527b7a88b0cc1beacefa47be")
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
                                   print(dashboardResponse)
                                   dashboardBinding.deliveredorderTxt.text = dashboardResponse.counts.delivered_order_count
                                   dashboardBinding.pendingorderTxt.text = dashboardResponse.counts.pending_order_count
                                   dashboardBinding.returnorderTxt.text = dashboardResponse.counts.return_order_count
                                   dashboardBinding.totalsaletxt.text = dashboardResponse.counts.totalsales_order_count
                                   if (response.body() != null) {
                                       if (response.body()!!.error == "0") {
                                           if (dashboardResponse.orders.isNotEmpty()) {
                                               dashboardBinding.newOrdersRequestsViewRecyclerview.visibility =
                                                   View.VISIBLE
                                               dashboardBinding.noData.visibility = View.GONE
                                               adapter = DashboardAdapter(dashboardResponse.orders, applicationContext)

                                               dashboardBinding.newOrdersRequestsViewRecyclerview.adapter =
                                                   adapter
                                           } else {
                                               dashboardBinding.newOrdersRequestsViewRecyclerview.visibility =
                                                   View.GONE
                                               dashboardBinding.noData.visibility = View.VISIBLE

                                           }
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


                       }catch (e: TimeoutException){
                           showToast(getString(R.string.time_out))
                       }
                   }

                   override fun onFailure(call: Call<DashboardResponseModal>, t: Throwable) {
                     //  dashboardBinding.progressBarLay.visibility  = View.GONE
                       showToast(t.message.toString())
                   }

               })


           }catch (e: Exception){
               //dashboardBinding.progressBarLay.visibility = View.GONE
               showToast(e.message.toString())
           }

       }
//    fun update_status() {
//        try {
//            val ordersService = ApiClient.buildService(ApiInterface::class.java)
//            val requestCall = ordersService.UpdateStatus("8aa65e3657407c1819df1d7c298eba633a3f9a0c710228571ef90f51eee2353508cabcfd8c1c527b7a88b0cc1beacefa47be")
//            requestCall.enqueue(object : Callback<Logout_Response>{
//                override fun onResponse(
//                    call: Call<Logout_Response>,
//                    response: Response<Logout_Response>
//                ) {
//                    try {
//                        var res_here = response.body()!!;
//                        when{
//                            response.code() == 200 ->{
//
//                                if (res_here.error=="0")
//                                {
//                                    showToast(res_here.message)
//                                }
//
//                            }
//                            response.code() == 401 -> {
//                                showToast(getString(R.string.session_exp))
//
//                            }
//                            else -> {
//                                showToast(getString(R.string.server_error))
//                            }
//                        }
//
//
//                    }catch (e: TimeoutException){
//                        showToast(getString(R.string.time_out))
//                    }
//                }
//
//                override fun onFailure(call: Call<Logout_Response>, t: Throwable) {
//                    showToast(getString(R.string.time_out))
//                }
//
//            })
//
//
//        }catch (e: Exception){
//           // dashboardBinding.progressBarLay.visibility = View.GONE
//            showToast(e.message.toString())
//        }
//
//    }



    }
