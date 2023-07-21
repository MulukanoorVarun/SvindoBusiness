package com.example.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.example.vendorapp.databinding.ActivityPendingDocumentsBinding
import com.example.vendorapp.modelclass.Bankdetails_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


@SuppressLint("StaticFieldLeak")
lateinit var douctmentsverifbinding: ActivityPendingDocumentsBinding
lateinit var PanResponse: Bankdetails_Response
class PendingDocuments : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        setContentView(R.layout.activity_pending_documents)
        douctmentsverifbinding = ActivityPendingDocumentsBinding.inflate(layoutInflater)
        setContentView(douctmentsverifbinding.root)
        val error = getPreferences(MODE_PRIVATE).getString("error"," ");



        val imageBitmap = intent.getParcelableExtra<Bitmap>("image")
        if (error == "1") {
            douctmentsverifbinding.bankAccountTxt.setBackgroundResource(R.drawable.succesbuttonborder)
            douctmentsverifbinding.bankAccountTxt.setTextColor(Color.WHITE)
        }
        else{

            douctmentsverifbinding.bankAccountTxt.setBackgroundResource(R.drawable.boarderwithcolor)
            douctmentsverifbinding.bankAccountTxt.setTextColor(Color.BLACK)


        }




        douctmentsverifbinding.pancardDeatilsTxt.setOnClickListener {
            val intent = Intent(this, PancardActivity::class.java)
            startActivity(intent)
        }
        douctmentsverifbinding.bankAccountTxt.setOnClickListener {
            val intent = Intent(this, BankaccountActivity::class.java)
            startActivity(intent)
        }
        douctmentsverifbinding.contactTxt.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }
        douctmentsverifbinding.businessTxt.setOnClickListener {
            val intent = Intent(this, BusinessdetailsActivity::class.java)
            startActivity(intent)
        }
        douctmentsverifbinding.fssaitext.setOnClickListener {
            val intent = Intent(this,FssaiActivity::class.java)
            startActivity(intent)
        }
        douctmentsverifbinding.gstintxt.setOnClickListener {
            val intent = Intent(this,GstinActivity::class.java)
            startActivity(intent)
        }

    }




}
