package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.svindo.vendorapp.R

import com.svindo.vendorapp.databinding.ActivityAddAddonBinding
import com.svindo.vendorapp.modelclass.NotificationModal
import com.svindo.vendorapp.utils.SharedPreference


@SuppressLint("StaticFieldLeak", "Registered")

class AddAddonActivity : AppCompatActivity() {
    private lateinit var addadonbinding: ActivityAddAddonBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var addonResponse: NotificationModal
//    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
//    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
//    private var imageUri: Uri? = null
//    private  var file_1: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_addon)
    addadonbinding = ActivityAddAddonBinding.inflate(layoutInflater)
    //sharedPreference = SharedPreference(this)
    setContentView(addadonbinding.root)
}
}