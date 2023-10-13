
package `in`.webgrid.svindobusiness.activity

import `in`.webgrid.svindobusiness.Utils.showToast
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.modelclass.Bankdetails_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import`in`.webgrid.svindobusiness.databinding.ActivityBankaccountBinding
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var bankaccountdetailsbinding: ActivityBankaccountBinding
private lateinit var bankdetailsResponse:Bankdetails_Response
class BankaccountActivity : AppCompatActivity() {
    lateinit var progress: ProgressDialog
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        bankaccountdetailsbinding = ActivityBankaccountBinding.inflate(layoutInflater)
        setContentView(bankaccountdetailsbinding.root)


        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)

        bankaccountdetailsbinding.bankaccskipbtn.setOnClickListener {
            val i = Intent(this@BankaccountActivity,ContactActivity::class.java)
            startActivity(i)
        }
        bankaccountdetailsbinding.bankdetailsBackbutton.setOnClickListener {
            val intent = Intent(this, PendingDocuments::class.java)
            startActivity(intent)
        }

        bankaccountdetailsbinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        bankaccountdetailsbinding.bankdetailsSubmitbutton.setOnClickListener {
            bankaccountdetailsbinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            bankaccountdetailsbinding.bankdetailsSubmitbutton.setEnabled(false)
            Handler().postDelayed({
                bankaccountdetailsbinding.bankdetailsSubmitbutton.setEnabled(true)
                bankaccountdetailsbinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)


            val ifsc_code = bankaccountdetailsbinding.ifscEtTxt.text.toString().trim()
            val name = bankaccountdetailsbinding.accHolderNameEt.text.toString().trim()
            val bank_name = bankaccountdetailsbinding.bankNameEtTxt.text.toString().trim()
            val account_number = bankaccountdetailsbinding.accNumEtTxt.text.toString().trim()
            val accountreenternum = bankaccountdetailsbinding.reAccNumEtTxt.text.toString().trim()

            if (ifsc_code.isNotEmpty() && bank_name.isNotEmpty() && account_number.isNotEmpty() && accountreenternum.isNotEmpty() && name.isNotEmpty()) {
                bankaccountdetails(
                    bankaccountdetailsbinding.accHolderNameEt.text.toString().trim(),
                    bankaccountdetailsbinding.bankNameEtTxt.text.toString().trim(),
                    bankaccountdetailsbinding.ifscEtTxt.text.toString().trim(),
                    bankaccountdetailsbinding.accNumEtTxt.text.toString().trim(),
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun bankaccountdetails(
        name:String,
        bank_name: String,
        ifsc_code: String,
        account_number: String,

        ) {
        progress.show()

        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.bankaccountdetails(sharedPreference.getValueString("token"),"bank_account",name,ifsc_code,bank_name,account_number)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        bankdetailsResponse = response.body()!!
                        if (bankdetailsResponse.error=="0") {
                            progress.dismiss()
                            showToast(bankdetailsResponse.message)
                            val i = Intent(this@BankaccountActivity,ContactActivity::class.java)
                            startActivity(i)
                        } else{
                            progress.dismiss()
                            //showToast(bankdetailsResponse.message)
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
            override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                showToast(getString(R.string.session_exp))
            }
        })

    }
}