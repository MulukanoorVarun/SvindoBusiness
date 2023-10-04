package com.svindo.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.svindo.deliverypartner.utils.URIPathHelper
import com.svindo.vendorapp.R
import com.svindo.vendorapp.adapters.*
import com.svindo.vendorapp.databinding.ActivityBusinessdetailsBinding
import com.svindo.vendorapp.databinding.ActivityGstinBinding
import com.svindo.vendorapp.databinding.SpinneritemdesignBinding
import com.svindo.vendorapp.modelclass.*
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
import kotlin.system.exitProcess
import androidx.core.text.HtmlCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("StaticFieldLeak")
class BusinessdetailsActivity : AppCompatActivity() {
    lateinit var businessdetailsBinding: ActivityBusinessdetailsBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    lateinit var Response: Verify_otp_Response
    private lateinit var businessdetailsResponse: Verify_otp_Response
    private lateinit var sharedPreference: SharedPreference
    private lateinit var gstinBinding: ActivityGstinBinding
    private lateinit var binding: SpinneritemdesignBinding
    private lateinit var spinner: Spinner
    private val itemList: MutableList<Maincategory> = ArrayList()

    private var fusedLocationClient: FusedLocationProviderClient?=null

    private val cameraPermissionCode = 201
    val storagePermissionCode = 202
    private val emailPattern = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"


    var cat_id=""
    var sericeitem=""
    var Zone_id=""
    var subzone_id=""
    var myaddress=""
    var cityname=""


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(this)
        setContentView(R.layout.activity_businessdetails)
        businessdetailsBinding = ActivityBusinessdetailsBinding.inflate(layoutInflater)
        gstinBinding = ActivityGstinBinding.inflate(layoutInflater)
        setContentView(businessdetailsBinding.root)


        businessdetailsBinding.changePhoto.setOnClickListener {
            showAlertDialog()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        businessdetailsBinding.locationEt.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1000
                )
            }else{
                getLocation()
            }
        }


        val loginButton = findViewById<ImageView>(R.id.business_details_backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }


        //implementing Services Spinner view
        val Services = resources.getStringArray(R.array.services)
         spinner = findViewById(R.id.Servicesspinnerview)
        if (spinner != null) {
            val adapter =  ServicesAdapter(this, R.layout.spinneritemlayout, Services)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = Services[position]
                     sericeitem= selectedItem
                //    showToast(sericeitem)

                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
                    val bitmap = imageUri?.let { decodeUri(it) }
                    businessdetailsBinding.businessProfile.setImageBitmap(bitmap)

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

                        val bitmap = imageUri?.let { decodeUri(it) }
                        businessdetailsBinding.businessProfile.setImageBitmap(bitmap)

                         filePath?.let {
                                file_1 = compressImage(filePath, 0.5)
                            }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }
                }
            }

        businessdetailsBinding.checkBoxText.setOnClickListener {
            var termsurl="https://webgrid.in/projects/svindo/web/Webcontroller/terms"
            if (!termsurl.startsWith("http://") && !termsurl.startsWith("https://")) {
                termsurl = "http://$termsurl"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsurl))
            startActivity(browserIntent)
        }
        val htmltext="By proceeding, you agree to our <b>Terms & Conditions</b> and <b>Privacy Policy</b>."
        businessdetailsBinding.checkBoxText.text = HtmlCompat.fromHtml(htmltext, HtmlCompat.FROM_HTML_MODE_LEGACY)



//        businessdetailsBinding.businessDetailsBackbutton.setOnClickListener {

//        }
        businessdetailsBinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        businessdetailsBinding.bankdetailsSubmitbutton.setOnClickListener {
            businessdetailsBinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            businessdetailsBinding.bankdetailsSubmitbutton.setEnabled(false)
            Handler().postDelayed({
                businessdetailsBinding.bankdetailsSubmitbutton.setEnabled(true)
                businessdetailsBinding.bankdetailsSubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)




            val business_name = businessdetailsBinding.businessNameEt.text.toString().trim()
            val address = businessdetailsBinding.addressEt.text.toString().trim()
            val location = businessdetailsBinding.locationEt.text.toString().trim()
            val contact_mob_num = businessdetailsBinding.mobileNumEt.text.toString().trim()
            val store_email_id = businessdetailsBinding.storeEmailIdEt.text.toString().trim()
       //     if(businessdetailsBinding.checkBox.isChecked) {
                if (business_name.isNotEmpty() && address.isNotEmpty() && location.isNotEmpty() && contact_mob_num.length == 10 && contact_mob_num.isNotEmpty() && store_email_id.isNotEmpty() && store_email_id.matches(emailPattern.toRegex()) && address.isNotEmpty() && file_1 != null) {
                    if(businessdetailsBinding.checkBox.isChecked) {
                        businessdetails(
                            businessdetailsBinding.nameet.text.toString().trim(),
                            businessdetailsBinding.businessNameEt.text.toString().trim(),
                            businessdetailsBinding.locationEt.text.toString().trim(),
                            contact_mob_num.toString().trim(),
                            sharedPreference.getValueString("mobile_number").toString(),
                            store_email_id.toString().trim(),
                            business_category = cat_id.toString().trim(),
                            businessdetailsBinding.addressEt.text.toString().trim(),
                            sericeitem.toString().trim(),
                            subzone_id.toString().trim(),
                            file_1!!
                        )
                    }else{
                        businessdetailsBinding.checkBoxText.text="Please Accept it!"
                        businessdetailsBinding.checkBoxText.setTextColor(Color.parseColor("#FF0000"))
                        Handler().postDelayed({
                            businessdetailsBinding.checkBoxText.text= HtmlCompat.fromHtml(htmltext, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            businessdetailsBinding.checkBoxText.setTextColor(Color.parseColor("#000000"))
                        }, 3000)
                    }
                } else {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                    if (!store_email_id.matches(emailPattern.toRegex())) {
                        businessdetailsBinding.storeEmailIdEt.setError("Please Enter Mailid in correct format Ex:user@gmail.com")
                    }

                    if (contact_mob_num.length < 10) {
                        businessdetailsBinding.mobileNumEt.setError("Mobile number should be 10 digits")
                    }
                }
         //   }
//            else{
//                businessdetailsBinding.checkBox.text="Please Accept it!"
//                businessdetailsBinding.checkBox.setTextColor(Color.parseColor("#FF0000"))
//                Handler().postDelayed({
//                businessdetailsBinding.checkBox.text="By proceeding, you agree to our Terms & Conditions and Privacy Policy."
//                businessdetailsBinding.checkBox.setTextColor(Color.parseColor("#000000"))
//                }, 3000)
         //   }
        }

        CategoriesList()
        ZonesList()

    }

    @SuppressLint("SetTextI18n")
    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location : Location? ->
            if(location!=null){
                getAddress(location.latitude,location.longitude)
                businessdetailsBinding.locationEt.setText(location.latitude.toString()+" "+location.longitude.toString())
            }
        }
    }

    private fun getAddress(lat:Double,lon:Double){
        try {
            val geocoder= Geocoder(this, Locale.getDefault())
            val addresses=geocoder.getFromLocation(lat,lon,1)
            if(addresses!=null){
                myaddress=addresses[0].getAddressLine(0)
                cityname=addresses[0].locality
            }
        }catch (e:Exception){

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Really Exit?")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes)
            { arg0, arg1 ->
                setResult(RESULT_OK, Intent().putExtra("EXIT", true))
                moveTaskToBack(true)
                exitProcess(-1)
            }.create().show()
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
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//           // showToast("Hello")
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
    private fun decodeUri(uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeStream(inputStream, null, options)
    }
    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        galleryResultLauncher.launch(intent)
    }



//    private fun camera() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
//            // showToast("Hello")
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
//        }else {
//            openCamera()
//        }
//    }


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
                    getLocation()
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
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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
    fun ZonesList(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.Zones()
            requestCall.enqueue(object : Callback<ZonesModalClass> {
                override fun onResponse(
                    call: Call<ZonesModalClass>,
                    response: Response<ZonesModalClass>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            ZonesSpinner(response.body()!!.location_zone)
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

                override fun onFailure(call: Call<ZonesModalClass>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }

    internal fun ZonesSpinner(items: List<LocationZoneXX>) {
        spinner = findViewById(R.id.Zonesspinnerview)

        val adapter = ZonesSpinnerAdapter(this, items)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                Zone_id = selectedItem.id

//                showToast(Zone_id.toString())
                fetchItemToSubZones(Zone_id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?){
                // Do nothing when nothing is selected
            }
        }
    }
    fun fetchItemToSubZones(Zone_id: String
    ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.SubZones(Zone_id)
            requestCall.enqueue(object : Callback<ZonesModalClass> {
                override fun onResponse(
                    call: Call<ZonesModalClass>,
                    response: Response<ZonesModalClass>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                    if (response.body()!!.error == "0") {
                                        SubZoneSpinner(response.body()!!.location_sub_zone)
                                    } else {

                                    }
                                } else {

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

                override fun onFailure(call: Call<ZonesModalClass>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    internal fun SubZoneSpinner(items: List<LocationSubZone>) {
        spinner = findViewById(R.id.SubZonesspinnerview)

        val adapter = SubZonesSpinnerAdapter(this, items)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                subzone_id = selectedItem.id
//                showToast(subzone_id.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
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
    private fun businessdetails(
        name: String,
        business_name : String,
        location : String,
        contact_mob_num: String,
        login_num:String,
        store_email_id: String,
        business_category : String,
        address : String,
        serviceitem:String,
        subzone_id:String,
        file1: File
        ) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile2= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("logo", file1.name, requestFile2)
        val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val business_name: RequestBody = business_name.toRequestBody("text/plain".toMediaTypeOrNull())
        val contact_mob_num: RequestBody = contact_mob_num.toRequestBody("text/plain".toMediaTypeOrNull())
        val login_num: RequestBody = login_num.toRequestBody("text/plain".toMediaTypeOrNull())
        val store_email_id: RequestBody = store_email_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val business_category: RequestBody = business_category.toRequestBody("text/plain".toMediaTypeOrNull())
        val address: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val type: RequestBody ="business_details".toRequestBody("text/plain".toMediaTypeOrNull())
        val location: RequestBody =location.toRequestBody("text/plain".toMediaTypeOrNull())
        val serviceitem: RequestBody=serviceitem.toRequestBody("text/plain".toMediaTypeOrNull())
        val subzone_id: RequestBody=subzone_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestCall = loginService.businessdetails(type,business_name,login_num,store_email_id,business_category,address,location,name,subzone_id,body,serviceitem,contact_mob_num)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        businessdetailsResponse= response.body()!!

                        if (businessdetailsResponse.error=="0") {
                        //    print(businessdetailsResponse)
                                sharedPreference.save("token", businessdetailsResponse.token);
                                showToast(businessdetailsResponse.message)
                            if(sericeitem=="Food")
                            {
                                sharedPreference.save("gst_skip_enable","1")
                                val i = Intent(this@BusinessdetailsActivity, FssaiActivity::class.java)
                               i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(i)
                            }
                            else if(sericeitem=="Services")
                            {
                                sharedPreference.save("gst_skip_enable","1")
                                val i = Intent(this@BusinessdetailsActivity, GstinActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(i)
                            }
                            else if(sericeitem=="Product Sales"){
                                if(sericeitem=="Product Sales") {
                                    sharedPreference.save("gst_skip_enable","0")
//                                    gstinBinding.skipbtn.isEnabled = false
//                                    gstinBinding.skipbtn.isClickable = false
                                }
                                val i = Intent(this@BusinessdetailsActivity, GstinActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(i)
                            }
                        }else{
                            showToast(businessdetailsResponse.message)
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








