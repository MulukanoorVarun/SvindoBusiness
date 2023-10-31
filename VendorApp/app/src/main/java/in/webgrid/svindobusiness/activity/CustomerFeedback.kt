
package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.CustomerFeedbackAdapter
import`in`.webgrid.svindobusiness.databinding.ActivityCustomerFeedbackBinding
import`in`.webgrid.svindobusiness.modelclass.CustomerFeedbackModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("Registered")
class CustomerFeedback : AppCompatActivity() {
    private lateinit var feedbackBinding: ActivityCustomerFeedbackBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var feedbackresponse: CustomerFeedbackModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CustomerFeedbackAdapter
    lateinit var progress: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        feedbackBinding = ActivityCustomerFeedbackBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(feedbackBinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(false)
        progress.setCancelable(false)



        val loginButton = findViewById<ImageView>(R.id.feedbackbackbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        feedbackBinding.feedbackrv.layoutManager = linearLayoutManager
        feedbackBinding.feedbackrv.hasFixedSize()

        CustomerFeebackdetails()
    }


    fun CustomerFeebackdetails() {
        progress.show()
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.Customerfeedbackdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<CustomerFeedbackModal> {
                override fun onResponse(
                    call: Call<CustomerFeedbackModal>,
                    response: Response<CustomerFeedbackModal>
                ) {
                   progress.dismiss()
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                feedbackresponse = response.body()!!

                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        progress.dismiss()
                                        if (feedbackresponse.data.isNotEmpty()) {
                                            feedbackBinding.feedbackrv.visibility = View.VISIBLE
                                            // feedbackBinding.noData.visibility = View.GONE
                                            adapter = CustomerFeedbackAdapter(
                                                feedbackresponse.data,
                                                applicationContext
                                            )
                                            feedbackBinding.feedbackrv.adapter = adapter
                                        } else {
                                            feedbackBinding.feedbackrv.visibility = View.GONE
                                            //  feedbackBinding.noData.visibility = View.VISIBLE

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


                    } catch (e: TimeoutException) {
                        showToast(getString(R.string.time_out))
                    }
                }

                override fun onFailure(call: Call<CustomerFeedbackModal>, t: Throwable) {
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

