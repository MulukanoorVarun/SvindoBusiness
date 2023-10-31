package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.AdWalletAdapter
import`in`.webgrid.svindobusiness.databinding.ActivityAdWalletBinding
import`in`.webgrid.svindobusiness.modelclass.WalletModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

@SuppressLint("Registered")
class AdWalletActivity : AppCompatActivity() {
    lateinit var  walletbinding: ActivityAdWalletBinding
    lateinit var walletModalres: WalletModal
    lateinit var adapter: AdWalletAdapter
    private lateinit var sharedPreference: SharedPreference
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var progress: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletbinding = ActivityAdWalletBinding.inflate(layoutInflater)
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

        AdWalletdetails()
    }

    fun AdWalletdetails() {
        progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =ordersService.AdWalletDetails(sharedPreference.getValueString("token"))
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
                                        adapter = AdWalletAdapter(walletModalres.transaction)
                                        walletbinding.transactiondetailsrecyclerview.adapter = adapter


                                        walletbinding.amount.text =walletModalres.user_data.available_amount
//                                        walletbinding.salesamount.text=walletModalres.user_data.credited
//                                        walletbinding.withdrawamount.text=walletModalres.user_data.debited
//                                        walletbinding.submitbutton.setOnClickListener {
//                                            showAlertDialog((walletModalres.user_data.available_amount).toDouble())
                                      //  }
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
}