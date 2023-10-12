package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.CouponListAdapter
import`in`.webgrid.svindobusiness.databinding.ActivityAddNewCouponCodeBinding
import`in`.webgrid.svindobusiness.databinding.ActivityCouponsBinding
import`in`.webgrid.svindobusiness.modelclass.AddCouponModal
import`in`.webgrid.svindobusiness.modelclass.CouponsListModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")

lateinit var couponsBinding: ActivityCouponsBinding
class CouponsActivity : AppCompatActivity() {
    lateinit var addNewCouponCodeBinding: ActivityAddNewCouponCodeBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var couponresponse: CouponsListModal
    private lateinit var addcoupopnResponse:AddCouponModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CouponListAdapter
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        couponsBinding = ActivityCouponsBinding.inflate(layoutInflater)
        addNewCouponCodeBinding = ActivityAddNewCouponCodeBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(couponsBinding.root)
        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)
        sharedPreference= SharedPreference(this)
//        couponsBinding.addbutton.setOnClickListener {
//            val intent = Intent(this, AddNewCouponCode::class.java)
//            startActivity(intent)
//        }

        val loginButton = findViewById<ImageView>(R.id.backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        couponsBinding.couponsrecyclerview.layoutManager = linearLayoutManager
        couponsBinding.couponsrecyclerview.hasFixedSize()

        couponsBinding.addbutton.setOnClickListener {
          showAlertDialog()
        }


        CouponListDeatils()
    }

    internal fun showAlertDialog(){
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
//        val binding = addNewCouponCodeBinding.inflate(layoutInflater)
        val rootView = addNewCouponCodeBinding.root

        // Check if the rootView already has a parent
        val parent = rootView.parent as? ViewGroup
        parent?.removeView(rootView) // Remo

        builder.setView(rootView)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        addNewCouponCodeBinding.addcouponcodesubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        addNewCouponCodeBinding.addcouponcodesubmitbutton.setOnClickListener {
            addNewCouponCodeBinding.addcouponcodesubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            addNewCouponCodeBinding.addcouponcodesubmitbutton.setEnabled(false)
            Handler().postDelayed({
                addNewCouponCodeBinding.addcouponcodesubmitbutton.setEnabled(true)
                addNewCouponCodeBinding.addcouponcodesubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)


            val couponCode = addNewCouponCodeBinding.couponcode.text.toString().trim()
            val minamount = addNewCouponCodeBinding.minamt.text.toString().trim()
            val maximumamount = addNewCouponCodeBinding.maxamt.text.toString().trim()
            val discountpercentage = addNewCouponCodeBinding.dicountamt.text.toString().trim()
            val validitydays = addNewCouponCodeBinding.validitydays.text.toString().trim()
            val description = addNewCouponCodeBinding.coupondescpt.text.toString().trim()

            if (couponCode.isNotEmpty() && minamount.isNotEmpty() && maximumamount.isNotEmpty() && discountpercentage.isNotEmpty()  && description.isNotEmpty() && validitydays.isNotEmpty()){
                progress.show()
                AddCouponDetails(
                    addNewCouponCodeBinding.couponcode.text.toString().trim(),
                    addNewCouponCodeBinding.coupondescpt.text.toString().trim(),
                    addNewCouponCodeBinding.minamt.text.toString().trim(),
                    addNewCouponCodeBinding.maxamt.text.toString().trim(),
                    addNewCouponCodeBinding.dicountamt.text.toString().trim(),
                    addNewCouponCodeBinding.validitydays.text.toString().trim(),
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
           alertDialog.hide()
        }
    }

    fun CouponListDeatils() {
        try {
            // dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.CouponListDeatils(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<CouponsListModal> {
                override fun onResponse(
                    call: Call<CouponsListModal>,
                    response: Response<CouponsListModal>
                ) {
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when{
                            response.code() == 200 ->{
                                couponresponse = response.body()!!
                              //  print(couponresponse)
                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        if (couponresponse.list.isNotEmpty()) {
                                            couponsBinding.couponsrecyclerview.visibility = View.VISIBLE
                                            couponsBinding.noData.visibility = View.GONE
                                            adapter = CouponListAdapter(couponresponse.list, applicationContext)
                                            couponsBinding.couponsrecyclerview.adapter = adapter
                                        }
                                        else {
                                            couponsBinding.couponsrecyclerview.visibility = View.GONE
                                            couponsBinding.noData.visibility = View.VISIBLE

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

                override fun onFailure(call: Call<CouponsListModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }
            })
        }catch (e: Exception){
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }

    fun AddCouponDetails(
        couponCode: String,
        description: String,
        minamount: String,
        maximumamount: String,
        discountpercentage: String,
        validitydays: String,

        ) {
        progress.show()
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.AddCoupondetails(sharedPreference.getValueString("token"),couponCode,description,minamount,maximumamount,discountpercentage,validitydays)

        requestCall.enqueue(object : Callback<AddCouponModal> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<AddCouponModal>,
                response: Response<AddCouponModal>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 29
                        addcoupopnResponse = response.body()!!
                        if (addcoupopnResponse.error=="0") {
                            progress.dismiss()
                            CouponListDeatils()
                            showToast(addcoupopnResponse.message)
//                            val i = Intent(this@AddNewCouponCode,CouponsActivity::class.java)
//                            startActivity(i)
                        } else{
                            progress.dismiss()
                            //howToast(addcoupopnResponse.message)
                        }
                    }
                    response.code() == 401 -> {//unauthorised
                        showToast(getString(R.string.session_exp))
                    }
                    else -> {//Application-level failure
                        //status code in the range of 300's, 400's, and 500's
                        showToast(getString(R.string.server_error))
                    }
                }
            }

            override fun onFailure(call: Call<AddCouponModal>, t: Throwable) {
                progress.dismiss()
                showToast(getString(R.string.session_exp))
            }

        })

    }

}