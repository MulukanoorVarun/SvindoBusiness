package com.svindo.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.svindo.vendorapp.R
import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.svindo.vendorapp.adapters.AccountsAdapter
import com.svindo.vendorapp.databinding.ActivityPayOutAmountBinding
import com.svindo.vendorapp.databinding.WithdrawamountlayoutBinding
import com.svindo.vendorapp.modelclass.Verify_otp_Response
import com.svindo.vendorapp.services.ApiClient
import com.svindo.vendorapp.services.ApiInterface

import com.svindo.vendorapp.modelclass.WalletModal
import com.svindo.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException
import com.svindo.vendorapp.utils.showToast
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@SuppressLint("StaticFieldLeak")
class PayOutAmountActivity : AppCompatActivity() {
    lateinit var  walletbinding:ActivityPayOutAmountBinding
    lateinit var walletModalres: WalletModal
    lateinit var adapter: AccountsAdapter
    private lateinit var sharedPreference: SharedPreference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletbinding = ActivityPayOutAmountBinding.inflate(layoutInflater)
        setContentView(walletbinding.root)


        linearLayoutManager = LinearLayoutManager(this)
        sharedPreference=SharedPreference(this)
        walletbinding.transactiondetailsrecyclerview.layoutManager = linearLayoutManager
        walletbinding.transactiondetailsrecyclerview.hasFixedSize()



        val loginButton = findViewById<ImageView>(R.id.backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }
        WalletDetails()
    }
    internal fun showAlertDialog(amount_me:Double) {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = WithdrawamountlayoutBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)


        binding.submitbutton.setBackgroundResource(R.drawable.buttonbackground)
        binding.submitbutton.setOnClickListener {

            binding.submitbutton.setBackgroundResource(R.drawable.button_loading_background)
            binding.submitbutton.setEnabled(false)
            Handler().postDelayed({
                binding.submitbutton.setEnabled(true)
                binding.submitbutton.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)
             var amnt= binding.withdrawamtet.text.toString().trim()
            if((amnt).toDouble()<=amount_me){
                if(amount_me>0)
                {
                    WithDrawlDetails(amnt)
                }else{
                    Toast.makeText(this, "Please, Maintain minimum balance.", Toast.LENGTH_SHORT).show()
                }
                alertDialog.hide()
            }else{
                Toast.makeText(this, "Please, enter amount less then Rs.$amount_me", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "$amount_me", Toast.LENGTH_SHORT).show()

//            WithDrawlDetails(
//                amount:String
//            )
//            if (amnt.isNotEmpty()&& amnt<=Int.(amount)){
//                WithDrawlDetails(
//                    amount = binding.withdrawamtet.text.toString().trim()
//                )
//            }else {
//                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
//            }
//            alertDialog.hide()
        }
    }
        fun WalletDetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.WalletDetailsInterface(sharedPreference.getValueString("token"))
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
                                    walletbinding.transactiondetailsrecyclerview.visibility = View.VISIBLE
                                    adapter = AccountsAdapter(walletModalres.transaction)
                                    walletbinding.transactiondetailsrecyclerview.adapter = adapter


                                        walletbinding.amount.text =walletModalres.user_data.available_amount
                                        walletbinding.salesamount.text=walletModalres.user_data.credited
                                        walletbinding.withdrawamount.text=walletModalres.user_data.debited
                                        walletbinding.submitbutton.setOnClickListener {
                                            showAlertDialog((walletModalres.user_data.available_amount).toDouble())
                                        }
                                } else {
                                        walletbinding.transactiondetailsrecyclerview.visibility = View.GONE
//                                        accountbinding.noData.visibility = View.VISIBLE
                                }
                                }else{
                                   // showToast(walletModalres.message.toString())
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

    fun WithDrawlDetails(
        amount:String
    ) {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.WithDrawlAmount(sharedPreference.getValueString("token"),amount)
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {
                    try {
                        when {
                            response.code() == 200 -> {
                                response.body()!!
                                if (response.body() != null) {
                                    showToast(response.body()!!.message.toString())
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



