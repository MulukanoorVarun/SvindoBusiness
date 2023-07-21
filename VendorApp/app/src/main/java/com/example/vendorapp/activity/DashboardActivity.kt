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
    lateinit var dashboardResponse: DashboardResponse
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: DashboardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
       sharedPreference = SharedPreference(this)
       dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
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
               R.id.catalouge -> {
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
   }
       fun dashboarddetails() {
           try {
              // dashboardBinding.progressBarLay.visibility = View.VISIBLE
               val ordersService = ApiClient.buildService(ApiInterface::class.java)
               val requestCall = ordersService.Dashboarddetails(sharedPreference.getValueString("token"))
               requestCall.enqueue(object : Callback<DashboardResponse> {
                   override fun onResponse(
                       call: Call<DashboardResponse>,
                       response: Response<DashboardResponse>
                   ) {

                       //dashboardBinding.progressBarLay.visibility  = View.GONE
                       try {
                           when{
                               response.code() == 200 ->{
                                   dashboardResponse = response.body()!!
                                   dashboardBinding.cancelorderTxt.text = dashboardResponse.cancelled_orders_count
                                   dashboardBinding.returnorderTxt.text = dashboardResponse.returned_orders
                                   dashboardBinding.completedorderTxt.text = dashboardResponse.completed_orders_count
                                   dashboardBinding.pendingorderTxt.text = dashboardResponse.pending_orders_count
                                   if(dashboardResponse.new_orders_list.isNotEmpty()){

                                       dashboardBinding.newOrdersRequestsViewRecyclerview.visibility = View.VISIBLE
                                       dashboardBinding.noData.visibility = View.GONE
                                       adapter = DashboardAdapter(dashboardResponse.new_orders_list,applicationContext)

                                       dashboardBinding.newOrdersRequestsViewRecyclerview.adapter = adapter
                                   }else{
                                       dashboardBinding.newOrdersRequestsViewRecyclerview.visibility = View.GONE
                                       dashboardBinding.noData.visibility = View.VISIBLE

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

                   override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                     //  dashboardBinding.progressBarLay.visibility  = View.GONE
                       showToast(t.message.toString())
                   }

               })


           }catch (e: Exception){
               //dashboardBinding.progressBarLay.visibility = View.GONE
               showToast(e.message.toString())
           }

       }



    }
}