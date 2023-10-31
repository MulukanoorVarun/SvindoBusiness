package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.SpotlightSpinnerAdapter
import`in`.webgrid.svindobusiness.databinding.ActivityAddSpotlightBinding
import`in`.webgrid.svindobusiness.modelclass.*
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("Registered")
class AddSpotlightActivity : AppCompatActivity() {
    private lateinit var addspotlightBinding:ActivityAddSpotlightBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var spotlightResponse: Verify_otp_Response
    private lateinit var spinner: Spinner
    lateinit var progress: ProgressDialog

    var ItemId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spotlight)
        sharedPreference= SharedPreference(this)
        addspotlightBinding = ActivityAddSpotlightBinding.inflate(layoutInflater)
        setContentView(addspotlightBinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)


        val loginButton = findViewById<ImageView>(R.id.AddSpotlightbackbutton)
        loginButton.setOnClickListener {
            this.onBackPressed()
        }
        ProductsList()
    }
    fun ProductsList(){
        addspotlightBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.ProductDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductsModal> {
                override fun onResponse(
                    call: Call<ProductsModal>,
                    response: Response<ProductsModal>
                ) {
                    addspotlightBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            setupSpinner(response.body()!!.products)

                                        } else {

                                        }
                                    } else {

                                    }
                                } else {

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

                override fun onFailure(call: Call<ProductsModal>, t: Throwable) {
                    addspotlightBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    showToast(t.message.toString())
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    internal fun setupSpinner(items:List<Product>){
        spinner = findViewById(R.id.spotlightspinnerview)
        val adapter = SpotlightSpinnerAdapter(this,items)
        spinner.adapter = adapter
        // Handle item selection


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                ItemId = selectedItem.id



                addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
                addspotlightBinding.submitbutton.setOnClickListener {
                    progress.show()
                    addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.button_loading_background);
                    addspotlightBinding.submitbutton.setEnabled(false)
                    Handler().postDelayed({
                        addspotlightBinding.submitbutton.setEnabled(true)
                        addspotlightBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
                    }, 2000)

                    fetchItemDetails(ItemId)
                }
                // Do something with the selected item (e.g., display its ID or name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun fetchItemDetails(itemId: String) {
        progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AddSpotlight(sharedPreference.getValueString("token"),itemId)
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {
                    progress.dismiss()
                    try {
                        when {
                            response.code() == 200 -> {
                                spotlightResponse = response.body()!!
                                if (spotlightResponse.error == "0") {
                                    val i =
                                        Intent(this@AddSpotlightActivity, MainActivity::class.java)
                                    startActivity(i)
                                    progress.dismiss()
                                    showToast(spotlightResponse.message)
                                } else {
                                    progress.dismiss()
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
}