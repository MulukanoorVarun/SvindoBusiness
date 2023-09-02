package com.example.vendorapp.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.UnitsAdapter
import com.example.vendorapp.databinding.ActivityUnitsBinding
import com.example.vendorapp.modelclass.UnitModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("StaticFieldLeak")
private lateinit var unitBinding:ActivityUnitsBinding
@SuppressLint("Registered")
class UnitsActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var unitsResponse: UnitModal
    private lateinit var LayoutManager: GridLayoutManager
    lateinit var adapter: UnitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        unitBinding = ActivityUnitsBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(unitBinding.root)
        LayoutManager = GridLayoutManager(this,4)
        unitBinding.unitsRecyclerview.layoutManager = LayoutManager
        unitBinding.unitsRecyclerview.hasFixedSize()
        Unitdetails()
    }
    fun Unitdetails() {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.Unitdetails("8aa65e3657407c1819df1d7c298eba633a3f9a0c710228571ef90f51eee2353508cabcfd8c1c527b7a88b0cc1beacefa47be")
            requestCall.enqueue(object : Callback<UnitModal> {
                override fun onResponse(
                    call: Call<UnitModal>,
                    response: Response<UnitModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when{
                            response.code() == 200 ->{

                                unitsResponse = response.body()!!
                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        if (unitsResponse.units.isNotEmpty()) {
                                            unitBinding.unitsRecyclerview.visibility = View.VISIBLE
                                            //  unitBinding.noData.visibility = View.GONE
                                            //    adapter = UnitsAdapter(unitsResponse.units,applicationContext)
                                            //   unitBinding.unitsRecyclerview.adapter = adapter
                                        } else {
//                                    unitBinding.unitsRecyclerview.visibility = View.GONE
                                            //unitBinding.noData.visibility = View.VISIBLE

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

                override fun onFailure(call: Call<UnitModal>, t: Throwable) {
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