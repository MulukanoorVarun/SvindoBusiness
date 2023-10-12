package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.databinding.ActivityContactBinding
import`in`.webgrid.svindobusiness.modelclass.Bankdetails_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var contactbinding : ActivityContactBinding
lateinit var contactResponse: Bankdetails_Response

class ContactActivity : AppCompatActivity() {
    lateinit var progress: ProgressDialog
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        contactbinding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(contactbinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)

        contactbinding.contactssubmitbutton.setBackgroundResource(R.drawable.buttonbackground)
        contactbinding.contactssubmitbutton.setOnClickListener {
            contactbinding.contactssubmitbutton.setBackgroundResource(R.drawable.button_loading_background)
            contactbinding.contactssubmitbutton.setEnabled(false)
            Handler().postDelayed({
                contactbinding.contactssubmitbutton.setEnabled(true)
                contactbinding.contactssubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)
            val emergency_mobile_number = contactbinding.mobNumEtTxt.text.toString().trim()
            val emergency_contact_name = contactbinding.nameEtTxt.text.toString().trim()
            if (emergency_contact_name.isNotEmpty() && emergency_mobile_number.isNotEmpty() && emergency_mobile_number.length==10) {
                contactdetails(
                    contactbinding.nameEtTxt.text.toString().trim(),
                    contactbinding.mobNumEtTxt.text.toString().trim(),
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                contactbinding.mobNumEtTxt.setError("Mobile number should be of 10 digits")
            }
        }



        contactbinding.contactbackbutton.setOnClickListener {
            val intent = Intent(this, PendingDocuments::class.java)
            startActivity(intent)
        }



        contactbinding.contactskipbtn.setOnClickListener{
                val i = Intent(this@ContactActivity ,MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            }


        }
    private fun contactdetails(
        emergency_contact_name: String,
        emergency_mobile_number: String,
    ) {
        progress.show()
//        showToast(emergency_mobile_number)
//        showToast(emergency_contact_name)
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestCall = loginService.contactdetails(sharedPreference.getValueString("token"),"contact",emergency_contact_name, emergency_mobile_number)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            //if you receive http response then this method will executed
            //your status code decide if your http response is a success or failure
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        contactResponse = response.body()!!
                        if (contactResponse.error=="0") {
                            progress.dismiss()
                            response.body()?.let { showToast(it.message)}
                            sharedPreference.clearSharedPreference()
                            val i = Intent(this@ContactActivity ,LoginActivity::class.java)
//                            intent.putExtra("KEY_EXTRA", key)
                             i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                            startActivity(i)
                        }
                        else{
                            progress.dismiss()
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
