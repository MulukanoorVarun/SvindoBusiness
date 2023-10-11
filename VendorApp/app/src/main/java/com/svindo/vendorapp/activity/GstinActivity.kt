package com.svindo.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import com.svindo.deliverypartner.utils.URIPathHelper
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.ActivityGstinBinding
import com.svindo.vendorapp.modelclass.Bankdetails_Response
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


@SuppressLint("StaticFieldLeak")
private lateinit var gstbinding: ActivityGstinBinding
class GstinActivity : AppCompatActivity(){
    private lateinit var gstinResponse: Bankdetails_Response
    private lateinit var sharedPreference: SharedPreference
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    private val cameraPermissionCode = 201
    private val storagePermissionCode = 202
    lateinit var progress: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        gstbinding = ActivityGstinBinding.inflate(layoutInflater)
        setContentView(gstbinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)


        val loginButton = findViewById<ImageView>(R.id.gst_backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }

                    val bitmap = imageUri?.let { decodeUri(it) }
                    gstbinding.Gstinimage.setImageBitmap(bitmap)

                    filePath?.let {
                        file_1 = compressImage(filePath, 0.5)
                    }
                    showToast("Image Selected")
                }
                else{
                    showToast("Image Not Selected")
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
                        gstbinding.Gstinimage.setImageBitmap(bitmap)

                        filePath?.let {
                            file_1 = compressImage(filePath, 0.5)
                        }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }
                }
            }

        gstbinding.camerabutton.setOnClickListener {
            showAlertDialog()
        }



        var skip_value=(sharedPreference.getValueString("gst_skip_enable")=="0")
        if(skip_value) {
            run {
                var btnview = findViewById<View>(R.id.gstskipbtn)
                btnview.isVisible = false;
            }
        }else {
            gstbinding.gstskipbtn.setOnClickListener {
                val intent = Intent(this, PancardActivity::class.java)
                startActivity(intent)
            }
        }
        gstbinding.gstinsubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        gstbinding.gstinsubmitbutton.setOnClickListener {
            gstbinding.gstinsubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            gstbinding.gstinsubmitbutton.setEnabled(false)
            Handler().postDelayed({
                gstbinding.gstinsubmitbutton.setEnabled(true)
                gstbinding.gstinsubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)



            val gstnum = gstbinding.gstinEtTxt.text.toString().trim()

            if (gstnum.isNotEmpty() && file_1!=null && gstnum.length==15) {
                gstdetails(
                    gstnum=gstbinding.gstinEtTxt.text.toString().trim(),
                    file_1!!
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                if(gstnum.length<15){
                    gstbinding.gstinEtTxt.setError("GSTIN number should be of 15 characters.Ex:29ABCDE1234F1ZW")
                }
            }
        }
    }
    private fun showAlertDialog() {
        val array = arrayOf(getString(R.string.gallery), getString(R.string.camera), getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_source))
        builder.setItems(array) { _, which ->
            when (which) {
                0 -> {
                    gallery()
                }
                1 -> {
                    camera()
                }
                else -> {

                }
            }

        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun gallery() {

//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                showToast("Hello")
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//            }else {
//                openGallery()
//            }

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



    private fun camera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionCode)
        }else if((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storagePermissionCode)
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionCode)
            }else{
                openCamera()
            }
        } else{
            openCamera()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            cameraPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                        camera()
                    }
                }else{
                    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else if (Manifest.permission.CAMERA == Manifest.permission.CAMERA) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionCode) }

                }
            }
            storagePermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        camera()
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




    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        resultLauncher.launch(intent)
    }



    fun compressImage(filePath: String, targetMB: Double = 1.0) : File {
        var file = File(filePath)
        var fullSizeBitmap: Bitmap = BitmapFactory.decodeFile(filePath)
        val exif = ExifInterface(filePath)
        val exifOrientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )
//        val exifDegree: Int = exifOrientationToDegrees(exifOrientation)
//        fullSizeBitmap = rotateImage(fullSizeBitmap, exifDegree.toFloat())

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


    private fun gstdetails(
        gstnum: String,
        file1: File
    ) {
        progress.show()

        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile1= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body1 = MultipartBody.Part.createFormData("gstin_image", file1.name, requestFile1)
        val type: RequestBody = "gstin".toRequestBody("text/plain".toMediaTypeOrNull())
        val gstnum: RequestBody = gstnum.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestCall = loginService.GSTINDeatils(sharedPreference.getValueString("token"),type,gstnum,body1)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        gstinResponse = response.body()!!
                        if (gstinResponse.error=="0") {
                            progress.dismiss()
                            showToast(gstinResponse.message)
                            val i = Intent(this@GstinActivity,PancardActivity::class.java)
                            startActivity(i)
                        } else{
                            progress.dismiss()
                           // showToast(gstinResponse.message)
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