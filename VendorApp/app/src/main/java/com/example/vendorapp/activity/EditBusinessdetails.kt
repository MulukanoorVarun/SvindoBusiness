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
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.exifinterface.media.ExifInterface
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R
import com.example.vendorapp.adapters.SpinnerItemsAdapter
import com.example.vendorapp.databinding.ActivityEditBusinessdetailsBinding
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.getFileSizeInMB
import com.example.vendorapp.utils.showToast
import com.squareup.picasso.Picasso
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

class EditBusinessdetails : AppCompatActivity() {

    private lateinit var binding:ActivityEditBusinessdetailsBinding
    private lateinit var pancradresponse: Verify_otp_Response
    private lateinit var fssaiResponse: Bankdetails_Response
    private lateinit var EditResponse: EditBusinessDetailsModal
    private lateinit var sharedPreference: SharedPreference
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var spinner: Spinner
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    private val cameraPermissionCode = 201
    private val storagePermissionCode = 202
    private val emailPattern = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    var selected_index=0
    var cat_id=""
    var image_file=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        binding = ActivityEditBusinessdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val loginButton = findViewById<ImageView>(R.id.editbusiness_details_backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }


        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }

                    val bitmap = imageUri?.let { decodeUri(it) }

                    if (selected_index==1) {
                        binding.Fssaiimage.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }
                    if (selected_index==2) {
                        binding.Gstimage.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }
                    if (selected_index==3) {
                        binding.PANimage.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }
                    if (selected_index==4) {
                        binding.businessProfile.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }
                    if (selected_index==5) {
                        binding.bannerImage.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }
                    showToast("Image Selected")
                }else{
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

                        if (selected_index==1) {
                            binding.Fssaiimage.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }
                        if (selected_index==2) {
                            binding.Gstimage.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }
                        if (selected_index==3) {
                            binding.PANimage.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }
                        if (selected_index==4) {
                            binding.businessProfile.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }
                        if (selected_index==5) {
                            binding.bannerImage.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }
                }
            }


        binding.pancamerabutton.setOnClickListener {
            selected_index=3
            showAlertDialog()
        }

        binding.fssaicamerabutton.setOnClickListener {
            selected_index=1
            showAlertDialog()
        }

        binding.gstcamerabutton.setOnClickListener {
            selected_index=2
            showAlertDialog()
        }

        binding.changePhoto.setOnClickListener {
            selected_index=4
            showAlertDialog()
        }

        binding.bannerImage.setOnClickListener {
            selected_index=5
            showAlertDialog()
        }

        binding.pansubmitbutton.setBackgroundResource(R.drawable.buttonbackground)
        binding.pansubmitbutton.setOnClickListener {
            binding.pansubmitbutton.setBackgroundResource(R.drawable.button_loading_background)
            binding.pansubmitbutton.setEnabled(false)

            Handler().postDelayed({
                 binding.pansubmitbutton.setEnabled(true)
                binding.pansubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            var panNumber=binding.panNOet.toString().trim()
            if(panNumber.isNotEmpty()&&file_1!=null&& panNumber.length==10) {
                uploadprofile(
                    file_1!!,
                    panNumber=binding.panNOet.toString().trim())
            }else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                if(panNumber.length<10) {
                    pancardBinding.panNOet.setError("PAN number should be of 10 characters")
                }
            }
        }


        binding.fssaisubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        binding.fssaisubmitbutton.setOnClickListener {

            binding.fssaisubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            binding.fssaisubmitbutton.setEnabled(false)
            Handler().postDelayed({
                binding.fssaisubmitbutton.setEnabled(true)
                binding.fssaisubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)
            var fssainum = binding.fssaiEtTxt.text.toString().trim()
            if ( fssainum.isNotEmpty()&&file_1!=null&&fssainum.length==19 && fssainum.startsWith("FSSAI")){
                fssaidetails(
                    fssainum=binding.fssaiEtTxt.text.toString().trim(),
                    file_1!!
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                if(fssainum.length<19 || !fssainum.startsWith("FSSAI")){
                    binding.fssaiEtTxt.setError("FSSAI number should be a 14-digit alphanumeric code and must starts with FSSAI.EX:FSSAI12345678901234")
                }
            }
        }

        binding.gstsubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        binding.gstsubmitbutton.setOnClickListener {
            binding.gstsubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            binding.gstsubmitbutton.setEnabled(false)
            Handler().postDelayed({
                binding.gstsubmitbutton.setEnabled(true)
                binding.gstsubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)


            val gstnum = binding.gstinEtTxt.text.toString().trim()

            if ( gstnum.isNotEmpty()&&file_1!=null&&gstnum.length==15) {
                gstdetails(
                    gstnum=binding.gstinEtTxt.text.toString().trim(),
                    file_1!!
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.banksubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        binding.banksubmitbutton.setOnClickListener {
            binding.banksubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            binding.banksubmitbutton.setEnabled(false)
            Handler().postDelayed({
                binding.banksubmitbutton.setEnabled(true)
                binding.banksubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            val ifsc_code = binding.ifscEtTxt.text.toString().trim()
            val bank_name = binding.bankNameEtTxt.text.toString().trim()
            val account_number = binding.accNumEtTxt.text.toString().trim()
            val accountreenternum = binding.reAccNumEtTxt.text.toString().trim()

            if (ifsc_code.isNotEmpty() && bank_name.isNotEmpty() && account_number.isNotEmpty() && accountreenternum.isNotEmpty()) {
                bankaccountdetails(
                    binding.bankNameEtTxt.text.toString().trim(),
                    binding.ifscEtTxt.text.toString().trim(),
                    binding.accNumEtTxt.text.toString().trim(),
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }




        binding.editdetailssubmitbutton.setBackgroundResource(R.drawable.buttonbackground)
        binding.editdetailssubmitbutton.setOnClickListener {
            binding.editdetailssubmitbutton.setBackgroundResource(R.drawable.button_loading_background)
            binding.editdetailssubmitbutton.setEnabled(false)
            Handler().postDelayed({
                binding.editdetailssubmitbutton.setEnabled(true)
                binding.editdetailssubmitbutton.setBackgroundResource(R.drawable.buttonbackground)
            }, 2000)

             //showToast(image_file.toString())

            val business_name = binding.businessnameet.text.toString().trim()
            val contact_mob_num = binding.mobileNumEt.text.toString().trim()
            val store_email_id = binding.storeEmailIdEt.text.toString().trim()
//            val bus_category = binding.businessCategoryEt.text.toString().trim()
            val address = binding.addressEt.text.toString().trim()
            val location = binding.locationEt.text.toString().trim()

            if (business_name.isNotEmpty() && contact_mob_num.isNotEmpty() && contact_mob_num.length==10 && store_email_id.isNotEmpty()&&store_email_id.matches(emailPattern.toRegex())&&address.isNotEmpty() && file_1!=null && location.isNotEmpty()){
                businessdetails(
                    binding.businessnameet.text.toString().trim(),
                    binding.mobileNumEt.text.toString().trim(),
                    binding.storeEmailIdEt.text.toString().trim(),
                    cat_id=cat_id.toString().trim(),
                    binding.addressEt.text.toString().trim(),
                    binding.locationEt.text.toString().trim(),
                    file_1!!
                )
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                if (!store_email_id.matches(emailPattern.toRegex())) {
                    binding.storeEmailIdEt.setError("Please Enter Mailid in correct format Ex:user@gmail.com")
                }
                if (contact_mob_num.length<10) {
                    binding.mobileNumEt.setError("Mobile number should be 10 digits")
                }
            }
        }
        Editbusinessdetails()
        CategoriesList()
}
    private fun showAlertDialog(){
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


    fun CategoriesList() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.session_freecategoriesListforReg()
            requestCall.enqueue(object : Callback<CustomSpinAdapter> {
                override fun onResponse(
                    call: Call<CustomSpinAdapter>,
                    response: Response<CustomSpinAdapter>
                ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            setupSpinner(response.body()!!.maincategories)
                                        } else {
                                        }
                                    }else{

                                    }
                                }else{

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
                override fun onFailure(call: Call<CustomSpinAdapter>, t: Throwable){
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }

    internal fun setupSpinner(items:List<Maincategory>) {
        spinner = findViewById(R.id.categoryspinnerview)

        val adapter = SpinnerItemsAdapter(this, items)
        spinner.adapter = adapter
        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                cat_id= selectedItem.id
//                showToast(cat_id.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }


    private fun uploadprofile(
        file1: File,
        panNumber:String) {
        try {
            val uploadBillService = ApiClient.buildService(ApiInterface::class.java)
            val requestFile1= file1.asRequestBody("image/*".toMediaTypeOrNull())
            val body1 = MultipartBody.Part.createFormData("pan_image", file1.name, requestFile1)
            val headers: MutableMap<String, String> = HashMap()
            headers["Sessionid"] =  sharedPreference.getValueString("token")!!
            val type: RequestBody = "pan".toRequestBody("text/plain".toMediaTypeOrNull())
            val panNumber: RequestBody = panNumber.toRequestBody("text/plain".toMediaTypeOrNull())


            val requestCall = uploadBillService.UploadBusinessRegFilesInterface(headers,type,panNumber,body1)
            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) {
                    when {
                        response.code() == 200 -> {//status code between 200 to 299
                            pancradresponse = response.body()!!
                            when (pancradresponse.error) {
                                "0" -> {
                                    showToast(pancradresponse.message)
                                }
                                else -> {
                                    showToast(pancradresponse.message)
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



    fun fssaidetails(
        fssainum: String,
        file1: File,
    ) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile1= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body1 = MultipartBody.Part.createFormData("fssai_image", file1.name, requestFile1)
        val type: RequestBody = "fssai".toRequestBody("text/plain".toMediaTypeOrNull())
        val fssainum: RequestBody = fssainum.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestCall = loginService.FssaiDetails(sharedPreference.getValueString("token"),type,fssainum,body1)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        fssaiResponse = response.body()!!
                        if (fssaiResponse.error=="0") {

                        } else{
                            showToast(fssaiResponse.message)
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



    private fun gstdetails(
        gstnum: String,
        file1: File
    ) {

        val loginService = ApiClient.buildService(ApiInterface::class.java)


        val requestFile1 = file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body1 = MultipartBody.Part.createFormData("gstin_image", file1.name, requestFile1)
        val type: RequestBody = "gstin".toRequestBody("text/plain".toMediaTypeOrNull())
        val gstnum: RequestBody = gstnum.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestCall =
            loginService.GSTINDeatils(sharedPreference.getValueString("token"), type, gstnum, body1)
        requestCall.enqueue(object : Callback<Bankdetails_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Bankdetails_Response>,
                response: Response<Bankdetails_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        fssaiResponse = response.body()!!
                        if (fssaiResponse.error == "0") {

                        } else {
                            showToast(fssaiResponse.message)
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



        private fun bankaccountdetails(
            bank_name: String,
            ifsc_code: String,
            account_number: String,

            ) {

            val loginService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = loginService.bankaccountdetails(sharedPreference.getValueString("token"),"bank_account",ifsc_code,bank_name,account_number)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) {
                    when {
                        response.isSuccessful -> {//status code between 200 to 299
                            fssaiResponse = response.body()!!
                            if (fssaiResponse.error=="0") {

                            } else{
                                showToast(fssaiResponse.message)
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
    fun Editbusinessdetails(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.BusinessDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<EditBusinessDetailsModal>{
                override fun onResponse(
                    call: Call<EditBusinessDetailsModal>,
                    response: Response<EditBusinessDetailsModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                    EditResponse = response.body()!!
                                    if (EditResponse.error == "0") {
                                        binding.businessnameet.setText(EditResponse.details.store_name)
                                        binding.mobileNumEt.setText(EditResponse.details.mobile_number)
                                        binding.storeEmailIdEt.setText(EditResponse.details.email_id)
//                                        binding.businessCategoryEt.setText(EditResponse.details.category)
                                        binding.locationEt.setText(EditResponse.details.location)
                                        binding.panNOet.setText(EditResponse.details.pan_number)
                                        binding.fssaiEtTxt.setText(EditResponse.details.fssai_id)
                                        binding.gstinEtTxt.setText(EditResponse.details.gstin)
                                        binding.addressEt.setText(EditResponse.details.address)
                                        binding.ifscEtTxt.setText(EditResponse.details.ifsc)
                                        binding.accNumEtTxt.setText(EditResponse.details.account_number)
                                        binding.bankNameEtTxt.setText(EditResponse.details.bank_name)
                                        binding.reAccNumEtTxt.setText(EditResponse.details.account_number)
                                        binding.verifiedstatus.setText(EditResponse.details.status)

                                        if(EditResponse.details.status=="Verified"){
                                            binding.mobileNumEt.isEnabled=false
                                            binding.storeEmailIdEt.isEnabled=false
                                            binding.locationEt.isEnabled=false
                                            binding.addressEt.isEnabled=false
                                            binding.categoryspinnerview.isEnabled=false
                                            binding.fssaicamerabutton.isEnabled=false
                                            binding.fssaiEtTxt.isEnabled=false
                                            binding.fssaisubmitbutton.isEnabled=false
                                            binding.gstsubmitbutton.isEnabled=false
                                            binding.gstcamerabutton.isEnabled=false
                                            binding.gstinEtTxt.isEnabled=false
                                            binding.pansubmitbutton.isEnabled=false
                                            binding.pancamerabutton.isEnabled=false
                                            binding.panNOet.isEnabled=false
                                            binding.bankNameEtTxt.isEnabled=false
                                            binding.ifscEtTxt.isEnabled=false
                                            binding.accNumEtTxt.isEnabled=false
                                            binding.reAccNumEtTxt.isEnabled=false
                                            binding.banksubmitbutton.isEnabled=false
                                            binding.note.isVisible=false
                                        }

//                                        binding.storemail.text = EditResponse.details.email_id
                                        Picasso.get().load(EditResponse.details.image).into(binding.businessProfile)
                                        Picasso.get().load(EditResponse.details.fssai_img).into(binding.Fssaiimage)
                                        Picasso.get().load(EditResponse.details.pan_img).into(binding.PANimage)
                                        Picasso.get().load(EditResponse.details.gstin_img).into(binding.Gstimage)
                                        Picasso.get().load(EditResponse.details.banner).into(binding.bannerImage)
                                         image_file=EditResponse.details.image



                                        var deliveryStatus = EditResponse.details.free_delivery

                                    } else {

                                    }
                                }else {

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
                override fun onFailure(call: Call<EditBusinessDetailsModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(getString(R.string.session_exp))
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    private fun businessdetails(
        business_name : String,
        contact_mob_num: String,
        store_email_id: String,
        cat_id : String,
        address : String,
        location:String,
        file1: File
    ) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile2= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("logo", file1.name, requestFile2)
        val business_name: RequestBody = business_name.toRequestBody("text/plain".toMediaTypeOrNull())
        val contact_mob_num: RequestBody = contact_mob_num.toRequestBody("text/plain".toMediaTypeOrNull())
        val store_email_id: RequestBody = store_email_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val business_category: RequestBody = cat_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val address: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val type: RequestBody = "business_details".toRequestBody("text/plain".toMediaTypeOrNull())
        val location: RequestBody = location.toRequestBody("text/plain".toMediaTypeOrNull())


        val requestCall = loginService.Editbusinessdetails(sharedPreference.getValueString("token"),type,business_name,contact_mob_num,store_email_id,business_category,address,location,body)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        pancradresponse= response.body()!!
                        if (pancradresponse.error=="0") {

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


