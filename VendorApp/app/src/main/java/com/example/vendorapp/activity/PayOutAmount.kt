package com.example.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.modelclass.Accounts_Response
import com.example.vendorapp.modelclass.Logout_Response
import com.example.vendorapp.adapters.AccountsAdapter
import com.example.vendorapp.databinding.ActivityPayOutAmountBinding
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.services.SessionManager

import com.example.vendorapp.modelclass.WalletModal
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException
import com.example.vendorapp.utils.showToast

@SuppressLint("StaticFieldLeak")
lateinit var  walletbinding:ActivityPayOutAmountBinding

@SuppressLint("StaticFieldLeak")
lateinit var walletModalres: WalletModal
private lateinit var adapter: AccountsAdapter


class PayOutAmountActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletbinding = ActivityPayOutAmountBinding.inflate(layoutInflater)
        setContentView(walletbinding.root)
        linearLayoutManager = LinearLayoutManager(this)

        walletbinding.transactiondetailsrecyclerview.layoutManager = linearLayoutManager
        walletbinding.transactiondetailsrecyclerview.hasFixedSize()

        WalletDetails()
//        accountdetails()

    }


//
//private fun logoutfromapp(sessionId: String?) {
//
//    val loginService = ApiClient.buildService(ApiInterface::class.java)
//    val requestCall = loginService.logoutfromapp((sessionId) )
////        val requestCall = loginService.vehicaldetailsupdatedetails(SessionManager.getToken(),vehical_number, vehical_model)
//    requestCall.enqueue(object : Callback<Logout_Response> {
//        //if you receive http response then this method will executed
//        //your status code decide if your http response is a success or failure
//        @SuppressLint("SuspiciousIndentation")
//        override fun onResponse(
//            call: Call<Logout_Response>,
//            response: Response<Logout_Response>
//        ) {
//            when {
//                response.isSuccessful -> {//status code between 200 to 299
//                    if (response.isSuccessful) {
//
//                        val intent = Intent(this@PayOutAmount, SplashScreen::class.java)
//                        startActivity(intent)
//                        finish()
//
//                    }
//
//
//                    logoutResponse = response.body()!!
//
//
//
//                }
//
//                response.code() == 401 -> {//unauthorised
//                    showToast(getString(R.string.session_exp))
//                }
//                else -> {//Application-level failure
//                    //status code in the range of 300's, 400's, and 500's
//                    showToast(getString(R.string.server_error))
//                }
//            }
//
//
//        }
//
//        override fun onFailure(call: Call<Logout_Response>, t: Throwable) {
//
//            showToast(getString(R.string.session_exp))
//        }
//
//    })
//
//
//}


    fun WalletDetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.WalletDetailsInterface("8aa65e3657407c1819df1d7c298eba633a3f9a0c710228571ef90f51eee2353508cabcfd8c1c527b7a88b0cc1beacefa47be","2")
            requestCall.enqueue(object : Callback<WalletModal> {
                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<WalletModal>,
                    response: Response<WalletModal>
                ) {
                    try {
                        when {
                            response.code() == 200 -> {
                                walletModalres = response.body()!!
                                if (walletModalres.error=="0")
                                {
                                    if (walletModalres.transaction.isNotEmpty()) {

                                    walletbinding.transactiondetailsrecyclerview.visibility =
                                        View.VISIBLE
                                    adapter = AccountsAdapter(walletModalres.transaction)
                                    walletbinding.transactiondetailsrecyclerview.adapter = adapter

                                    walletbinding.amount.text =walletModalres.user_data.available_amount
                                        walletbinding.salesamount.text=walletModalres.user_data.available_amount
                                        walletbinding.withdrawamount.text=walletModalres.user_data.available_amount
//                                "₹ ${accountsResponse.profile.available_amount}".also {  walletbinding.amount.text = it }
                                } else {
                                        walletbinding.transactiondetailsrecyclerview.visibility = View.GONE
//                                        accountbinding.noData.visibility = View.VISIBLE
                                }
                                }else{
                                    showToast(walletModalres.message.toString())
                                }

//
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

                override fun onFailure(call: Call<WalletModal>, t: Throwable) {
//                        accountbinding.progressBarLay.visibility  = View.GONE
                showToast(t.message.toString())
//                Toast(,t.message.toString());
                }
            })
        } catch (e: Exception) {
        showToast(e.message.toString())
        }
    }
}



