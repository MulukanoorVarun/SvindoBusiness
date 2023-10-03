package com.svindo.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import com.svindo.deliverypartner.utils.URIPathHelper
import com.svindo.vendorapp.R
import com.svindo.vendorapp.adapters.NotificationAdapter
import com.svindo.vendorapp.databinding.ActivityNotificationsBinding
import com.svindo.vendorapp.fragements.AccountsFragment
import com.svindo.vendorapp.modelclass.NotificationModal
import com.svindo.vendorapp.modelclass.Verify_otp_Response
import com.svindo.vendorapp.services.ApiClient
import com.svindo.vendorapp.services.ApiInterface
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.getFileSizeInMB
import com.svindo.vendorapp.utils.showToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var notificationsBinding: ActivityNotificationsBinding
class NotificationsActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    lateinit var notificationResponse: NotificationModal
    lateinit var addResponse: Verify_otp_Response
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: NotificationAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    private val cameraPermissionCode = 201
    private val storagePermissionCode = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationsBinding = ActivityNotificationsBinding.inflate(layoutInflater)
        //sharedPreference = SharedPreference(this)
        setContentView(notificationsBinding.root)
sharedPreference=SharedPreference(this)

        notificationsBinding. cardclicking.setOnClickListener {
            showAlertDialog()
        }


        val loginButton = findViewById<ImageView>(R.id.backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }

                    val bitmap = imageUri?.let { decodeUri(it) }
                    notificationsBinding.notificationImg.setImageBitmap(bitmap)

                    filePath?.let { file_1 = compressImage(filePath, 0.5) }
                }

            }

        galleryResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    imageUri = data!!.data
                    val uriPathHelper = URIPathHelper()
                    try {

                        val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
                        val bitmap = imageUri?.let { decodeUri(it) }
                        notificationsBinding.notificationImg.setImageBitmap(bitmap)
                        filePath?.let {
                            file_1 = compressImage(filePath, 0.5)
                        }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }

                }

            }

        linearLayoutManager = LinearLayoutManager(this)

        notificationsBinding.notificationsRecyclerview.layoutManager = linearLayoutManager
        notificationsBinding.notificationsRecyclerview.hasFixedSize()
        notificationdetails()


        notificationsBinding.notificationsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        notificationsBinding.notificationsSubmitbutton.setOnClickListener {
            notificationsBinding.notificationsSubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            notificationsBinding.notificationsSubmitbutton.setEnabled(false)
            Handler().postDelayed({
                notificationsBinding.notificationsSubmitbutton.setEnabled(true)
                notificationsBinding.notificationsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)



            val description = notificationsBinding.notidescettxt.text.toString().trim()

            if (description.isNotEmpty()&&file_1!=null){
                Addnotifications(
                    notificationsBinding.notidescettxt.text.toString().trim(),
                    file_1!!
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAlertDialog() {
        val array = arrayOf(getString(R.string.gallery),getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_source))
        builder.setItems(array) { _, which ->
            when (which) {
                0 -> {
                    gallery()
                }
                else -> {

                }
            }

        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun gallery() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//            showToast("Hello")
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//        }else {
//            openGallery()
//        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1)
        }else {
            openGallery()
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        galleryResultLauncher.launch(intent)

    }

    private fun decodeUri(uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeStream(inputStream, null, options)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            storagePermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        // camera()
                    }
                }else{
                    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storagePermissionCode) }

                }
            }
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        openGallery()
                    }
                }else{
                    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1) }

                }
            }
        }
    }
    fun compressImage(filePath: String, targetMB: Double = 1.0) : File {
        var file = File(filePath)
        var fullSizeBitmap: Bitmap = BitmapFactory.decodeFile(filePath)
        val exif = ExifInterface(filePath)
        val exifOrientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )
        try {

            val fileSizeInMB = getFileSizeInMB(filePath)

            var quality = 100
            if(fileSizeInMB > targetMB){//1.0 means target MB
                quality = ((targetMB/fileSizeInMB)*100).toInt()
            }
            val fileOutputStream = FileOutputStream(filePath)
            fullSizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.close()
            file = File(filePath)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return file
    }


    fun notificationdetails(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.notificationdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<NotificationModal>{
                override fun onResponse(
                    call: Call<NotificationModal>,
                    response: Response<NotificationModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                notificationResponse = response.body()!!
                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        if (notificationResponse.list.isNotEmpty()) {
                                            notificationsBinding.notificationsRecyclerview.visibility = View.VISIBLE
                                            notificationsBinding.noData.visibility = View.GONE
                                            adapter = NotificationAdapter(notificationResponse.list, applicationContext)
                                            notificationsBinding.notificationsRecyclerview.adapter = adapter

                                        } else {
                                            notificationsBinding.notificationsRecyclerview.visibility = View.GONE
                                            notificationsBinding.noData.visibility = View.VISIBLE
                                        }
                                    }
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

                override fun onFailure(call: Call<NotificationModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                    }

            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    private fun Addnotifications(
        description: String,
        file1: File
    ) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile2= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file1.name, requestFile2)
        val description: RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestCall = loginService.AddNotifications(sharedPreference.getValueString("token"),description,body)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        addResponse= response.body()!!
                        if (addResponse.error=="0") {
                            notificationdetails()
                            showToast(addResponse.message.toString())
                        }
                        if(addResponse.error=="1"){
                           showToast(addResponse.message)
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
            override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                showToast(getString(R.string.session_exp))
            }
        })
    }
}




