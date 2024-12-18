package `in`.webgrid.svindobusiness.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import`in`.webgrid.svindobusiness.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.adapters.AccountsAdapter
import`in`.webgrid.svindobusiness.databinding.ActivityPayOutAmountBinding
import`in`.webgrid.svindobusiness.databinding.WithdrawamountlayoutBinding
import`in`.webgrid.svindobusiness.modelclass.Verify_otp_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface

import`in`.webgrid.svindobusiness.modelclass.WalletModal
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException
import `in`.webgrid.svindobusiness.Utils.showToast
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
    lateinit var progress: ProgressDialog

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletbinding = ActivityPayOutAmountBinding.inflate(layoutInflater)
        setContentView(walletbinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(false)
        progress.setCancelable(false)

        linearLayoutManager = LinearLayoutManager(this)
        sharedPreference= SharedPreference(this)
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
            progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.WalletDetailsInterface(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<WalletModal> {
                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<WalletModal>,
                    response: Response<WalletModal>
                ) {
                    progress.dismiss()
                    try {
                        when {
                            response.code() == 200 -> {
                                walletModalres = response.body()!!
                                if (walletModalres.error=="0")
                                {
                                    progress.dismiss()
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
                                    progress.dismiss()
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
                    progress.dismiss()
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
        progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.WithDrawlAmount(sharedPreference.getValueString("token"),amount)
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {
                    progress.dismiss()
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
                    progress.dismiss()
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



