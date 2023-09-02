package com.example.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R

import com.example.vendorapp.databinding.ActivityAddAddonBinding
import com.example.vendorapp.databinding.ActivityNotificationsBinding
import com.example.vendorapp.modelclass.NotificationModal
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.getFileSizeInMB
import com.example.vendorapp.utils.showToast
import java.io.File
import java.io.FileOutputStream


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