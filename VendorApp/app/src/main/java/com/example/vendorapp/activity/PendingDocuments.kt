package com.example.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.example.vendorapp.databinding.ActivityPendingDocumentsBinding



@SuppressLint("StaticFieldLeak")
lateinit var douctmentsverifbinding: ActivityPendingDocumentsBinding

class PendingDocuments : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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