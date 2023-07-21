package com.example.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R
import com.example.vendorapp.databinding.ActivityBusinessdetailsBinding
import com.example.vendorapp.modelclass.Bankdetails_Response
import com.example.vendorapp.modelclass.Mobileotp_Response
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.services.SessionManager
import com.example.vendorapp.utils.getFileSizeInMB
import com.example.vendorapp.utils.rotateImage
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
import java.io.FileOutputStream
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Spinner
import com.example.vendorapp.utils.SharedPreference


@SuppressLint("StaticFieldLeak")
lateinit var businessdetailsBinding: ActivityBusinessdetailsBinding
//private lateinit var businessdetailsResponse:Bankdetails_Response
private lateinit var resultLauncher: ActivityResultLauncher<Intent>
private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
private var imageUri: Uri? = null
private  var file_1: File? = null
private lateinit var sharedPreference: SharedPreference
lateinit var Response: Verify_otp_Response
private lateinit var businessdetailsResponse: Verify_otp_Response
class BusinessdetailsActivity : AppCompatActivity() {
    lateinit var imageIV: ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        setContentView(R.layout.activity_businessdetails)




//        val languages = resources.getStringArray(R.array.business_categories)
//        // create an array adapter and pass the required parameter
//        // in our case pass the context, drop down layout , and array.
//        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, languages)
//        // get reference to the autocomplete text view
//        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.categories)
//        // set adapter to the autocomplete tv to the arrayAdapter
//        autocompleteTV.setAdapter(arrayAdapter)


       /* val spinner: Spinner = findViewById(R.id.spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.business_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }*/



        businessdetailsBinding = ActivityBusinessdetailsBinding.inflate(layoutInflater)
        setContentView(businessdetailsBinding.root)

        businessdetailsBinding.changePhoto.setOnClickListener {
//            val intent = Intent(this, PendingDocuments::class.java)
//            startActivity(intent)
//            business_profile
            showAlertDialog()
//            imageIV = findViewById(R.id.business_profile)
//            val imgBitmap = BitmapFactory.decodeFile(file_1!!.absolutePath)
//
//            // on below line we are setting bitmap to our image view.
//            imageIV.setImageBitmap(imgBitmap)

        }




        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
                   filePath?.let {
                            file_1 = compressImage(filePath, 0.5)
                        }
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
                         filePath?.let {
                                file_1 = compressImage(filePath, 0.5)

                             imageIV = findViewById(R.id.business_profile)
                             imageIV.getLayoutParams().height = 350;
                             imageIV.getLayoutParams().width = 350;

                                val imgBitmap = BitmapFactory.decodeFile(file_1!!.absolutePath)

                                // on below line we are setting bitmap to our image view.
                                imageIV.setImageBitmap(imgBitmap)


                            }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }

                }

            }





        businessdetailsBinding.businessDetailsBackbutton.setOnClickListener {

//            val intent = Intent(this, PendingDocuments::class.java)
//            startActivity(intent)
        }



        businessdetailsBinding.bankdetailsSubmitbutton.setOnClickListener {
            val name = businessdetailsBinding.nameet.text.toString().trim()
            val business_name = businessdetailsBinding.businessNameEt.text.toString().trim()
            val contact_mob_num = businessdetailsBinding.mobileNumEt.text.toString().trim()
            val store_email_id = businessdetailsBinding.storeEmailIdEt.text.toString().trim()
           // val business_category = businessdetailsBinding.businessCategoryEt.text.toString().trim()
            val address = businessdetailsBinding.addressEt.text.toString().trim()

            if (business_name.isNotEmpty() && contact_mob_num.isNotEmpty() && store_email_id.isNotEmpty() &&  address.isNotEmpty()) {


                businessdetails(
                    businessdetailsBinding.nameet.text.toString().trim(),
                    businessdetailsBinding.businessNameEt.text.toString().trim(),
                    businessdetailsBinding.mobileNumEt.text.toString().trim(),
                    businessdetailsBinding.storeEmailIdEt.text.toString().trim(),
                    businessdetailsBinding.businessCategoryEt.text.toString().trim(),
                    businessdetailsBinding.addressEt.text.toString().trim(),
                    file_1!!
                )


            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            showToast("Hello")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
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



    private fun camera() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionCode)
//        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storagePermissionCode)
//        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionCode)
//            }else{
//                openCamera()
//            }
//        }else {
        openCamera()
//        }
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




    private fun businessdetails(
        name: String,
        business_name : String,
        contact_mob_num: String,
        store_email_id: String,
        business_category : String,
        address : String,
        file1: File

        ) {

        val loginService = ApiClient.buildService(ApiInterface::class.java)

        val requestFile2= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("logo", file1.name, requestFile2)


        val name: RequestBody = business_name.toRequestBody("text/plain".toMediaTypeOrNull())
        val business_name: RequestBody = business_name.toRequestBody("text/plain".toMediaTypeOrNull())
        val type: RequestBody = "business_details".toRequestBody("text/plain".toMediaTypeOrNull())
        val contact_mob_num: RequestBody = contact_mob_num.toRequestBody("text/plain".toMediaTypeOrNull())
        val store_email_id: RequestBody = store_email_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val business_category: RequestBody = business_category.toRequestBody("text/plain".toMediaTypeOrNull())
        val address: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val loc: RequestBody = "17.439793598348952, 78.43828890255993".toRequestBody("text/plain".toMediaTypeOrNull())
        val requestCall = loginService.businessdetails(type,name,business_name,contact_mob_num,business_category,store_email_id,address,loc,body)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299

                        if (response.isSuccessful) {
                            businessdetailsResponse= response.body()!!


                            if (businessdetailsResponse.error=="0")
                            {
                                print(businessdetailsResponse)
                                sharedPreference.save("token", businessdetailsResponse.token);
                                showToast(businessdetailsResponse.message)
                                val i = Intent(this@BusinessdetailsActivity, BankaccountActivity::class.java)
                                startActivity(i)
                            }
                            else{
                                showToast(businessdetailsResponse.message)
                            }
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


    private fun uploadprofile(file1: File
    ) {
        try {

            val uploadBillService = ApiClient.buildService(ApiInterface::class.java)


            val requestFile1= file1.asRequestBody("image/*".toMediaTypeOrNull())
            val body1 = MultipartBody.Part.createFormData("adhar_front", file1.name, requestFile1)
            val headers: MutableMap<String, String> = HashMap()
//            headers["Sessionid"] =  sharedPreference.getValueString("token")!!

            val type: RequestBody = "addhar".toRequestBody("text/plain".toMediaTypeOrNull())

//            val phonenumber: RequestBody = sec_phonenumber.toRequestBody("text/plain".toMediaTypeOrNull())
//            val sec_phonenumber: RequestBody =  sec_phonenumber.toRequestBody("text/plain".toMediaTypeOrNull())
//            val fathername: RequestBody = fathername.toRequestBody("text/plain".toMediaTypeOrNull())
//            val dod: RequestBody = dod.toRequestBody("text/plain".toMediaTypeOrNull())
//            val gen: RequestBody = "Male".toRequestBody("text/plain".toMediaTypeOrNull())
//            val pincode: RequestBody = "500045".toRequestBody("text/plain".toMediaTypeOrNull())
//            val mail: RequestBody = "ak@123".toRequestBody("text/plain".toMediaTypeOrNull())
//            val loc: RequestBody = "17.446445818443724, 78.44861066842567".toRequestBody("text/plain".toMediaTypeOrNull())
//            val city: RequestBody = city.toRequestBody("text/plain".toMediaTypeOrNull())
//            val complete_address: RequestBody = complete_address.toRequestBody("text/plain".toMediaTypeOrNull())


            val requestCall = uploadBillService.UploadBusinessRegFilesInterface(
                headers,
                type,
                body1
            )
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {
                    when {
                        response.code() == 200 -> {//status code between 200 to 299
                            Response = response.body()!!
                            when (Response.error) {
                                "0" -> {
                               //  sharedPreference.save("in_registration_process", "1")
                                    startActivity(Intent(this@BusinessdetailsActivity,PancardActivity::class.java))
                                    showToast(Response.message)
                                }
                                else -> {
                                    showToast(Response.message)
                                }
                            }
                        }
                        response.code() == 401 -> {
                            showToast(getString(R.string.session_exp))
//                                startActivity(Intent(this@MyprofileActivity, LoginActivity::class.java))
                        }
                        else -> {//Application-level failure
                            //status code in the range of 300's, 400's, and 500's
                            showToast(getString(R.string.server_error))
                        }
                    }
                }

                //invoked in case of Network Error or Establishing connection with Server
                //or Error Creating Http Request or Error Processing Http Response
                override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                    showToast(getString(R.string.server_error))
                }
            })

        } catch (e: java.lang.Exception) {
            showToast(e.message.toString())
        }
    }




}







