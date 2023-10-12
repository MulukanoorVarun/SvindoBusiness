package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.BannersAdapter
import`in`.webgrid.svindobusiness.adapters.DashboardAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentHomeBinding
import`in`.webgrid.svindobusiness.modelclass.*
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


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
        dashboardBinding = FragmentHomeBinding.inflate(layoutInflater)
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardBinding = FragmentHomeBinding.inflate(inflater, container, false)
        dashboardBinding = FragmentHomeBinding.inflate(layoutInflater)

        linearLayoutManager = LinearLayoutManager(context)
        dashboardBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        dashboardBinding.newordersRequestsViewRecyclerview.hasFixedSize()
       // dashboardBinding.newordersRequestsViewRecyclerview.setNestedScrollingEnabled(false)
        var session_id=sharedPreference.getValueString("session")


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
    }

    fun dashboarddetails() {
        dashboardBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.Dashboarddetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<DashboardResponseModal> {
                override fun onResponse(
                    call: Call<DashboardResponseModal>,
                    response: Response<DashboardResponseModal>
                ) {
                    dashboardBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when{
                            response.code() == 200 ->{
                                dashboardResponse = response.body()!!
                                    if(dashboardResponse !=null) {
                                        if (dashboardResponse.error == "0") {
                                            dashboardBinding.deliveredorderTxt.text = dashboardResponse.counts.delivered_order_count
                                            dashboardBinding.pendingorderTxt.text = dashboardResponse.counts.pending_order_count
                                            dashboardBinding.returnorderTxt.text = dashboardResponse.counts.return_order_count
                                            dashboardBinding.totalsaletxt.text = dashboardResponse.counts.totalsales_order_count
                                            dashboardBinding.textview1.text = dashboardResponse.counts.store_details.store_name

                                            dashboardBinding.shopstatusswitch.setOnClickListener() {
                                                if (dashboardResponse.counts.store_details.open_status == "Open"){
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
                    dashboardBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e: Exception){
          //  dashboardBinding.progressBarLay.visibility= View.GONE
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



