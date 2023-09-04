package com.example.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R
import com.example.vendorapp.adapters.BannersListAdapter
import com.example.vendorapp.adapters.ProductsAdapter
import com.example.vendorapp.adapters.ProductsListAdapter
import com.example.vendorapp.adapters.ServicesAdapter
import com.example.vendorapp.databinding.ActivityAddBannersBinding
import com.example.vendorapp.databinding.ProductslistrecyclerviewlayoutBinding
import com.example.vendorapp.modelclass.BannersListModal
import com.example.vendorapp.modelclass.ProductsModal
import com.example.vendorapp.modelclass.Verify_otp_Response
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


@SuppressLint("StaticFieldLeak")

class AddBannersActivity : AppCompatActivity() {
    private lateinit var bannersBinding: ActivityAddBannersBinding
    private lateinit var binding: ProductslistrecyclerviewlayoutBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var bannersResponse:BannersListModal
    lateinit var addBannerResponse:Verify_otp_Response
    lateinit var productsresponse: ProductsModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager1: LinearLayoutManager
    private lateinit var adapter: BannersListAdapter
    private lateinit var adapter1: ProductsListAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    private var dialog:Dialog? = null
    private val cameraPermissionCode = 201
    private val storagePermissionCode = 202
    lateinit var productsarrayList: ArrayList<String>;

    var itemId = ""
    private lateinit var spinner: Spinner
    var BannerItem=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bannersBinding = ActivityAddBannersBinding.inflate(layoutInflater)
       // sharedPreference = SharedPreference(this)
        setContentView(bannersBinding.root)


        sharedPreference=SharedPreference(this)
        bannersBinding.cardclicking.setOnClickListener {
            showAlertDialog()
        }
        val Product_id= intent.getStringExtra("product_id")

        val loginButton = findViewById<ImageView>(R.id.backbutton)
        loginButton.setOnClickListener { this.onBackPressed()
        }
        val Services = resources.getStringArray(R.array.redirects)
        spinner = findViewById(R.id.Bannersspinnerview)
        if (spinner != null){
            val adapter =  ServicesAdapter(this, R.layout.spinneritemlayout, Services)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                    val selectedItem = Services[position]
                    BannerItem= selectedItem
                    showToast(BannerItem)
                    if(BannerItem=="Products")
                    {
                        bannersBinding.cardview.isVisible=true
                        bannersBinding.productname.isVisible=true
                        Productdetails() // Make
                        //dialog!!.dismiss()
                    }
                    if (BannerItem == "Stores"){
                        bannersBinding.cardview.isVisible=false
                        bannersBinding.productname.isVisible=false
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>){
                    // write code to perform some action
                }
            }
        }
        bannersBinding.bannerssubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
        bannersBinding.bannerssubmitbutton.setOnClickListener {
            bannersBinding.bannerssubmitbutton.setBackgroundResource(R.drawable.button_loading_background);
            bannersBinding.bannerssubmitbutton.setEnabled(false)
            Handler().postDelayed({
                bannersBinding.bannerssubmitbutton.setEnabled(true)
                bannersBinding.bannerssubmitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            var redirectType=BannerItem.toString().trim()
            var max_amt=bannersBinding.maxamtet.text.toString().trim()

            if(redirectType.isNotEmpty()&& file_1!=null && max_amt.isNotEmpty()) {
                AddBanners(
                    redirectType = BannerItem.toString().trim(),
                    itemId=itemId.toString().trim(),
                    max_amt=bannersBinding.maxamtet.text.toString().trim(),
                    file_1!!
                )
            }else{
                showToast("Please select All fields")
            }
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }

                    val bitmap = imageUri?.let { decodeUri(it) }
                    bannersBinding.bannerImage.setImageBitmap(bitmap)

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
                        bannersBinding.bannerImage.setImageBitmap(bitmap)

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
        bannersBinding.BannersRecyclerview.layoutManager = linearLayoutManager
        bannersBinding.BannersRecyclerview.hasFixedSize()



        BannersListdetails()
    }


//    internal fun showProductDialog() {
//        dialog = Dialog(this@AddBannersActivity)
//        binding=ProductslistrecyclerviewlayoutBinding.inflate(layoutInflater)
//        dialog!!.setContentView(binding.root)
//        // Set custom height and width
//        dialog!!.window?.setLayout(650, 800)
//
//        linearLayoutManager1 = LinearLayoutManager(this@AddBannersActivity)
//        binding.productsRecyclerview.layoutManager = linearLayoutManager1
//        binding.productsRecyclerview.hasFixedSize()
//
//        Productdetails() // Make sure this function p
//        dialog!!.show()
//    }
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

    private fun openGallery() {
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
    fun BannersListdetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.BannersListDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<BannersListModal> {
                override fun onResponse(
                    call: Call<BannersListModal>,
                    response: Response<BannersListModal>
                ) {
                    try {
                        when{
                            response.code() == 200 -> {
                                bannersResponse = response.body()!!
                                if (response.body() != null) {
                                    if (bannersResponse.error == "0") {

                                        bannersBinding.viewamt.setText(bannersResponse.cost.per_view_cost)
                                        bannersBinding.perclickcostamt.setText(bannersResponse.cost.per_click_cost)

                                        if (bannersResponse.banner_list.count() > 0) {
                                            bannersBinding.BannersRecyclerview.visibility = View.VISIBLE
                                            bannersBinding.noData.visibility = View.GONE
                                            adapter = BannersListAdapter(bannersResponse.banner_list, applicationContext)
                                            bannersBinding.BannersRecyclerview.adapter = adapter
                                        } else {
                                            bannersBinding.BannersRecyclerview.visibility = View.GONE
                                            bannersBinding.noData.visibility = View.VISIBLE
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


                    }catch (e: TimeoutException){
                        showToast(getString(R.string.time_out))
                    }
                }

                override fun onFailure(call: Call<BannersListModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })
        }catch (e: Exception){
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }

    private fun AddBanners(
        redirectType: String,
        itemId:String,
        max_amt:String,
        file1: File
    ) {
        val loginService = ApiClient.buildService(ApiInterface::class.java)
        val requestFile2= file1.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("banner", file1.name, requestFile2)
        val RedirectType: RequestBody = redirectType.toRequestBody("text/plain".toMediaTypeOrNull())
        val itemId: RequestBody = itemId.toRequestBody("text/plain".toMediaTypeOrNull())
        val max_amt: RequestBody = max_amt.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestCall = loginService.AddBanners(sharedPreference.getValueString("token"),RedirectType,itemId,max_amt,body)
        requestCall.enqueue(object : Callback<Verify_otp_Response> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<Verify_otp_Response>,
                response: Response<Verify_otp_Response>
            ) {
                when {
                    response.isSuccessful -> {//status code between 200 to 299
                        addBannerResponse= response.body()!!
                        if (addBannerResponse.error=="0") {
                            BannersListdetails()
                            //    sharedPreference.save("token", businessdetailsResponse.token);
                            showToast(addBannerResponse.message)
                        }else{
                            showToast(addBannerResponse.message)
                        }
                        if(addBannerResponse.error=="1"){
                            showToast(addBannerResponse.message)
                        }
                        if(addBannerResponse.error=="2"){
                            showToast(addBannerResponse.message)
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


    fun Productdetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.ProductDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductsModal> {
                override fun onResponse(
                    call: Call<ProductsModal>,
                    response: Response<ProductsModal>
                ) =
                    try {
                        when {
                            response.code() == 200 -> {
                                    productsresponse = response.body()!!
                                     if (response.body() != null) {
                                    if (productsresponse.error == "0") {

                                        productsarrayList=ArrayList()
                                        if(productsresponse.products.size>0)
                                        {
                                            for (i in productsresponse.products)
                                            {
                                                productsarrayList!!.add(i.name)
                                            }
                                            setupSpinner(productsarrayList!!)
                                        } else {

                                        }
//                                            binding.productsRecyclerview.visibility = View.VISIBLE
//                                            adapter1 = ProductsListAdapter(productsresponse.products,applicationContext)
//                                            binding.productsRecyclerview.adapter = adapter1
                                    } else {
                                        showToast("List is Empty")
                                        binding.productsRecyclerview.visibility = View.GONE
                                    }
                                }
                                else{

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
                override fun onFailure(call: Call<ProductsModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }
            })

        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }


    internal fun setupSpinner(items:ArrayList<String>) {
        val textview = findViewById<TextView>(R.id.productname)
        val imageview = findViewById<ImageView>(R.id.imageview)
            // Initialize dialog
            dialog = Dialog(this@AddBannersActivity)

            // Set custom dialog layout
            dialog!!.setContentView(R.layout.searchablespinnerlayout)

            // Set custom height and width
            dialog!!.window?.setLayout(700, 800)

            // Set transparent background
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Show dialog
            dialog!!.show()
            // Initialize variables from dialog
            val editText = dialog!!.findViewById<EditText>(R.id.edit_text)
            val listView = dialog!!.findViewById<ListView>(R.id.list_view)

            // Initialize custom adapter
//            val customAdapter = CustomProductsSoinnerAdapter(items, this)
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@AddBannersActivity,
                android.R.layout.simple_list_item_1,
                items
            )
            // Set adapter
            listView.adapter = adapter

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            listView.setOnItemClickListener { parent, view, position, id ->
                // When item selected from list
                // Set selected item on textView
//               val selectedItem = items[position]
//                var  name=selectedItem
               textview.text = adapter.getItem(position).toString()
                // Dismiss dialog
//                itemId
                for (i in productsresponse.products)
                {
                    if(i.name==textview.text)
                    {
                        itemId=i.id
                        Picasso.get().load(i.image).into(imageview)
                        showToast(itemId.toString())
                    }
                }
                dialog!!.dismiss()
            }
        }




}