package com.example.vendorapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R
import com.example.vendorapp.adapters.*
import com.example.vendorapp.databinding.ActivityAddNewProductBinding
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.getFileSizeInMB
import com.example.vendorapp.utils.showToast
import com.google.gson.Gson
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
private lateinit var Binding:ActivityAddNewProductBinding
class AddNewProduct : AppCompatActivity() {
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    private  var file_2: File? = null
    private  var file_3: File? = null
    private  var file_4: File? = null
    private  var file_5: File? = null

    private lateinit var spinner: Spinner
    private lateinit var spinneritem: Spinner

    var MainCatId=""
    var ItemId=""
    var subcat_id=""
    var size_id=""
    var size_name=""

    var insatantDel="0"
    var self_pick="0"
    var GeneralDel="0"

    var Return="0"
    var COD="0"
    var Replacement="0"
    var shopExchange="0"

    var is_printing="0"
    var selected_index=1

    private val itemList: MutableList<Unititem> = ArrayList()

    private lateinit var sharedPreference: SharedPreference
    private val cameraPermissionCode = 201
    private val storagePermissionCode = 202

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(Binding.root)


        sharedPreference=SharedPreference(this)
        val loginButton = findViewById<ImageView>(R.id.business_details_backbutton)
        loginButton.setOnClickListener {
            this.onBackPressed()
        }

        Binding.cardview.setOnClickListener {
            selected_index=1
            showAlertDialog()
        }

        Binding.cardview1.setOnClickListener {
            selected_index=2
            showAlertDialog()
        }
        Binding.cardview2.setOnClickListener {
            selected_index=3
            showAlertDialog()
        }
        Binding.cardview3.setOnClickListener {
            selected_index=4
            showAlertDialog()
        }
//        Binding.cardview4.setOnClickListener {
//            selected_index=5
//            showAlertDialog()
//        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uriPathHelper = URIPathHelper()
                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
                    val bitmap = imageUri?.let { decodeUri(it) }

                    if(selected_index==1) {
                        Binding.imageview.setImageBitmap(bitmap)
                        filePath?.let { file_1 = compressImage(filePath, 0.5) }
                    }else if(selected_index==2){
                        Binding.imageview1.setImageBitmap(bitmap)
                        filePath?.let { file_2 = compressImage(filePath, 0.5) }
                    }
                    else if(selected_index==3){
                        Binding.imageview2.setImageBitmap(bitmap)
                        filePath?.let { file_3 = compressImage(filePath, 0.5) }
                    }
                    else if(selected_index==4){
                        Binding.imageview3.setImageBitmap(bitmap)
                        filePath?.let { file_4= compressImage(filePath, 0.5) }
                    }
//                    else if(selected_index==5) {
//                        Binding.imageview4.setImageBitmap(bitmap)
//                        filePath?.let { file_5 = compressImage(filePath, 0.5) }
//                    }
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
                        if(selected_index==1) {
                            Binding.imageview.setImageBitmap(bitmap)
                            filePath?.let { file_1 = compressImage(filePath, 0.5) }
                        }else if(selected_index==2){
                            Binding.imageview1.setImageBitmap(bitmap)
                            filePath?.let { file_2 = compressImage(filePath, 0.5) }
                        }
                        else if(selected_index==3){
                            Binding.imageview2.setImageBitmap(bitmap)
                            filePath?.let { file_3 = compressImage(filePath, 0.5) }
                        }
                        else if(selected_index==4){
                            Binding.imageview3.setImageBitmap(bitmap)
                            filePath?.let { file_4= compressImage(filePath, 0.5) }
                        }
//                        else if(selected_index==5) {
//                            Binding.imageview4.setImageBitmap(bitmap)
//                            filePath?.let { file_5 = compressImage(filePath, 0.5) }
//                        }
                        showToast("Image Selected")
                    } catch (e: Exception) {
                        showToast("Image Not Selected")
                    }
                }
            }

        Binding.Selfpickupswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                self_pick="1";
            //    showToast(self_pick)
            }else{
                self_pick="0";
             //   showToast(self_pick)
            }
        }
        Binding.insatantswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                insatantDel="1";
            //    showToast(insatantDel)
            }else{
                insatantDel="0";
             //   showToast(insatantDel)
            }
        }
        Binding.generaldeliveryswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                GeneralDel="1"
              //  showToast(GeneralDel)
            }else{
                GeneralDel="0"
               // showToast(GeneralDel)
            }
        }
        Binding.returnswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                Return="1"
              //  showToast(Return)
            }else{
                Return="0"
                //showToast(Return)
            }
        }
        Binding.codswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                COD="1";
             ///   showToast(COD)
            }else{
                COD="0";
               // showToast(COD)
            }
        }
        Binding.replacementswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                Replacement="1";
              //  showToast(Replacement)
            }else{
                Replacement="0";
               // showToast(Replacement)
            }
        }
        Binding.shopexchangeswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                shopExchange="1";
              //  showToast(shopExchange)
            }else{
                shopExchange="0";
                //showToast(shopExchange)
            }
        }

        Binding.printingswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                is_printing ="1"
                Binding.discountprice.text="Price per piece"
                Binding.priceet.isEnabled = false
                Binding.spinner1.isEnabled = false
                Binding.stocket.isEnabled = false
               // showToast(is_printing)
            } else {
                is_printing ="0"
                Binding.discountprice.text="Sale Price"
                Binding.priceet.isEnabled = true
                Binding.spinner1.isEnabled = true
                Binding.stocket.isEnabled = true
            //    showToast(is_printing)
            }
        }


        Binding.submitbutton.setOnClickListener {
            val name = Binding.productnameet.text.toString().trim()
            val deliverydays = Binding.generaldeliverydays.text.toString().trim()
            val description = Binding.productdescriptionet.text.toString().trim()
            val mrp_price = Binding.priceet.text.toString().trim()
            val sale_price = Binding.discountpriceet.text.toString().trim()
            val stock = Binding.stocket.text.toString().trim()
            val quantiy = Binding.quantityet.text.toString().trim()
            val minquantiy = Binding.minquantityet.text.toString().trim()
            val brandName = Binding.brandet.text.toString().trim()
            val modelNumber = Binding.modelnoet.text.toString().trim()
            val color = Binding.coloret.text.toString().trim()
            if (file_1 != null && file_2 != null && file_3 != null && file_4 != null) {
                AddNewProductDetails(
                    category_id = subcat_id.toString().trim(),
                    unit_id = ItemId.toString().trim(),
                    size_id=size_id.toString().trim(),
                    insatantDel = insatantDel.toString().trim(),
                    deliverydays = Binding.generaldeliverydays.text.toString().trim(),
                    GeneralDel = GeneralDel.toString().trim(),
                    self_pick = self_pick.toString().trim(),
                    COD = COD.toString().trim(),
                    Replacement = Replacement.toString().trim(),
                    Return = Return.toString().trim(),
                    shopExchange = shopExchange.toString().trim(),
                    description = Binding.productdescriptionet.text.toString().trim(),
                    mrp_price = Binding.priceet.text.toString().trim(),
                    sale_price = Binding.discountpriceet.text.toString().trim(),
                    stock = Binding.stocket.text.toString().trim(),
                    name = Binding.productnameet.text.toString().trim() + " " + Binding.brandet.text.toString().trim() +" " + size_name.toString() + " " + Binding.modelnoet.text.toString()
                        .trim() + " " + Binding.coloret.text.toString().trim(),
                    quantiy = Binding.quantityet.text.toString().trim(),
                    minquantiy = Binding.minquantityet.text.toString().trim(),
                    subCategoryid = subcat_id.toString().trim(),
                    file1 = file_1!!,
                    file2 = file_2!!,
                    file3 = file_3!!,
                    file4 = file_4!!,
                    //    file5= file_5!!
                )
            }else{
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        MainCategoryList()
        Unitsdetails()
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
                1->{
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
    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        resultLauncher.launch(intent)
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

    fun Unitsdetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.Unitdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<UnitModal>{
                override fun onResponse(
                    call: Call<UnitModal>,
                    response: Response<UnitModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            setupSpinner(response.body()!!.units)
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

                override fun onFailure(call: Call<UnitModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }

    internal fun setupSpinner(items: List<Unititem>) {
        spinner = findViewById(R.id.spinner1)

        val adapter = UnitsAdapter(this, items)
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
                 var ItemId = selectedItem.id
            //    showToast(ItemId.toString())

                //  fetchItemDetailsCal(itemId)
                // Do something with the selected item (e.g., display its ID or name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun MainCategoryList() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.MainCategoreis(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<MainCategoryModal> {
                override fun onResponse(
                    call: Call<MainCategoryModal>,
                    response: Response<MainCategoryModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            MaincategorySpinner(response.body()!!.maincategories)
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

                override fun onFailure(call: Call<MainCategoryModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }

    internal fun MaincategorySpinner(items: List<MaincategoryX>) {
        spinner = findViewById(R.id.Mainspinnerview)

        val adapter = MainCategoryAdapter(this, items)
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
                // addItemToSpinner(selectedItem)
                MainCatId = selectedItem.id
              //  showToast(MainCatId.toString())

                fetchItemToSubcategory(MainCatId)
                // Do something with the selected item (e.g., display its ID or name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun fetchItemToSubcategory(cat_id: String
    ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.SubCategoryDetails(sharedPreference.getValueString("token"),cat_id)
            requestCall.enqueue(object : Callback<SubCategoryModal> {
                override fun onResponse(
                    call: Call<SubCategoryModal>,
                    response: Response<SubCategoryModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            SubcategorySpinner(response.body()!!.subcategories)
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

                override fun onFailure(call: Call<SubCategoryModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }
    internal fun SubcategorySpinner(items: List<Subcategory>) {
        spinner = findViewById(R.id.Subspinnerview)

        val adapter = SubCategoryAdapter(this, items)
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
                subcat_id = selectedItem.id
                SizesList(subcat_id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }
    fun SizesList(subcat_id:String){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.SubCategoryDetails(sharedPreference.getValueString("token"),subcat_id)
            requestCall.enqueue(object : Callback<SubCategoryModal> {
                override fun onResponse(
                    call: Call<SubCategoryModal>,
                    response: Response<SubCategoryModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            SizesSpinner(response.body()!!.sizes)
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

                override fun onFailure(call: Call<SubCategoryModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }
            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }
    internal fun SizesSpinner(items: List<Sizes>) {
        spinner = findViewById(R.id.Sizesspinnerview)

        val adapter = SizesAdapter(this, items)
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
                size_id = selectedItem.id
                size_name = selectedItem.size_name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }
    fun AddNewProductDetails(
        name:String,
        category_id:String,
        unit_id:String,
        size_id:String,
        deliverydays: String,
        insatantDel: String,
        GeneralDel:String,
        self_pick:String,
        Return:String,
        COD:String,
        Replacement:String,
        shopExchange:String,
        description:String,
        mrp_price:String,
        sale_price:String,
        stock:String,
        quantiy:String,
        minquantiy:String,
        subCategoryid:String,
        file1: File,
        file2: File,
        file3: File,
        file4: File,
       // file5: File,
    ) {
           //showToast(file1.toString())
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestFile1= file1.asRequestBody("image/*".toMediaTypeOrNull())
            val body1 = MultipartBody.Part.createFormData("default_img", file1.name, requestFile1)
            val requestFile2= file2.asRequestBody("image/*".toMediaTypeOrNull())
            val body2 = MultipartBody.Part.createFormData("default_img1", file2.name, requestFile2)
            val requestFile3= file3.asRequestBody("image/*".toMediaTypeOrNull())
            val body3 = MultipartBody.Part.createFormData("default_img2", file3.name, requestFile3)
            val requestFile4= file4.asRequestBody("image/*".toMediaTypeOrNull())
            val body4 = MultipartBody.Part.createFormData("default_img3", file4.name, requestFile4)
//            val requestFile5= file5.asRequestBody("image/*".toMediaTypeOrNull())
//            val body5 = MultipartBody.Part.createFormData("default_img", file5.name, requestFile5)
            val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val description: RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val origin: RequestBody = "India".toRequestBody("text/plain".toMediaTypeOrNull())
            val category_id: RequestBody = category_id.toRequestBody("text/plain".toMediaTypeOrNull())
            val mrp_price: RequestBody = mrp_price.toRequestBody("text/plain".toMediaTypeOrNull())
            val sale_price: RequestBody = sale_price.toRequestBody("text/plain".toMediaTypeOrNull())
            val quantiy: RequestBody = quantiy.toRequestBody("text/plain".toMediaTypeOrNull())
            val unit_id: RequestBody = unit_id.toRequestBody("text/plain".toMediaTypeOrNull())
            val stock: RequestBody = stock.toRequestBody("text/plain".toMediaTypeOrNull())
            val insatantDel: RequestBody = insatantDel.toRequestBody("text/plain".toMediaTypeOrNull())
            val GeneralDel: RequestBody =GeneralDel.toRequestBody("text/plain".toMediaTypeOrNull())
            val self_pick: RequestBody =self_pick.toRequestBody("text/plain".toMediaTypeOrNull())
            val Replacement: RequestBody =Replacement.toRequestBody("text/plain".toMediaTypeOrNull())
            val shopExchange: RequestBody =shopExchange.toRequestBody("text/plain".toMediaTypeOrNull())
            val Return: RequestBody =Return.toRequestBody("text/plain".toMediaTypeOrNull())
            val COD: RequestBody =COD.toRequestBody("text/plain".toMediaTypeOrNull())
            val deliverydays: RequestBody =deliverydays.toRequestBody("text/plain".toMediaTypeOrNull())
            val minquantiy: RequestBody =minquantiy.toRequestBody("text/plain".toMediaTypeOrNull())
            val subCategoryid: RequestBody =subCategoryid.toRequestBody("text/plain".toMediaTypeOrNull())
            val size_id: RequestBody =size_id.toRequestBody("text/plain".toMediaTypeOrNull())

            val requestCall = ordersService.AddNewProductDetails(sharedPreference.getValueString("token"),name,description,origin,category_id,mrp_price,sale_price,quantiy,unit_id,stock,insatantDel,GeneralDel,self_pick,Return,shopExchange,deliverydays,COD,Replacement,subCategoryid,minquantiy,size_id,body1,body2,body3,body4)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            showToast(response.body()!!.message.toString())
                                    val i = Intent(this@AddNewProduct,MainActivity::class.java)
                                    startActivity(i)

                                        } else {
                                           // showToast(response.body()!!.message.toString())
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

                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }

    }

//     fun addItemToSpinner(selectedItem: String) {
//        val adapter2 = spinneritem.adapter as ArrayAdapter<String>
//        adapter2.add(selectedItem)
//        adapter2.notifyDataSetChanged()
//
//    fun fetchItemToSubcategory(itemId: String){
//        try {
//            val ordersService = ApiClient.buildService(ApiInterface::class.java)
//            val requestCall =
//                ordersService.fetchItemToSubCategory("8aa65e3657407c1819df1d7c298eba633a3f9a0c710228571ef90f51eee2353508cabcfd8c1c527b7a88b0cc1beacefa47be",itemId)
//            requestCall.enqueue(object : Callback<SubCategoryModal> {
//                override fun onResponse(
//                    call: Call<SubCategoryModal>,
//                    response: Response<SubCategoryModal>
//                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
//                    try {
//                        when {
//                            response.code() == 200 -> {
//                                //data = response.body()!!
//                                if (response.isSuccessful) {
//                                    SubcategorySpinner(response.body()!!.subcategories)
//                                } else {
//
//                                }
//                            }
//                            response.code() == 401 -> {
//                                showToast(getString(R.string.session_exp))
//
//                            }
//                            else -> {
//                                showToast(getString(R.string.server_error))
//                            }
//                        }
//
//
//                    } catch (e: TimeoutException) {
//                        showToast(getString(R.string.time_out))
//                    }
//
//                override fun onFailure(call: Call<SubCategoryModal>, t: Throwable) {
//                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
//                    showToast(t.message.toString())
//                }
//
//            })
//
//
//        } catch (e: Exception) {
//            //dashboardBinding.progressBarLay.visibility = View.GONE
//            showToast(e.message.toString())
//        }
//
//    }
//    internal fun SubcategorySpinner(item: List<Subcategory>) {
//        spinner = findViewById(R.id.spinner_view1)
//
//        val adapter = SubCategoryAdapter(this, item)
//        spinner.adapter = adapter
//
//        // Handle item selection
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val selectedItem1 = item[position]
//                // addItemToSpinner(selectedItem)
//                val ItemId = selectedItem1.id
//                showToast(ItemId.toString())
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing when nothing is selected
//            }
//        }
//    }
}


