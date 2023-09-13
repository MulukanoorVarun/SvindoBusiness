package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vendorapp.R
import com.example.vendorapp.adapters.AddonsListSpinnerAdapter
import com.example.vendorapp.adapters.CustomSpinnerAdapter
import com.example.vendorapp.adapters.MainCategoryAdapter
import com.example.vendorapp.adapters.SubCategoryAdapter
import com.example.vendorapp.databinding.*
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var PrintingProductBinding: ActivityAddPrintingProductBinding

class AddPrintingProduct : AppCompatActivity() {
    private lateinit var Response: Bankdetails_Response
    private lateinit var sharedPreference: SharedPreference
    // Inside your Activity or Fragment
    private lateinit var spinner: Spinner
    private val itemList: MutableList<Maincategory> = ArrayList()

    var ItemId=""
    var Maincat_id=""
    var subcat_id=""


    var insatantDel="0"
    var self_pick="0"
    var GeneralDel="0"

    var Return="0"
    var COD="0"
    var Replacement="0"
    var shopExchange="0"

    var CLRData= ArrayList<ColourData>()
    var colors_show = ArrayList<String>()


    var FabriData= ArrayList<FabricData>()
    var fabric_show = ArrayList<String>()



    var PatrnData=ArrayList<PatternData>()
    var pattern_show = ArrayList<String>()


   var SizeData=ArrayList<SizesData>()
    var size_show = ArrayList<String>()

    var PriceData=ArrayList<PricesData>()
    var bundle_show = ArrayList<String>()


    var ADDONData= ArrayList<AddonsData>()
    var Addons_show=ArrayList<String>()


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PrintingProductBinding = ActivityAddPrintingProductBinding.inflate(layoutInflater)
        setContentView(PrintingProductBinding.root)

        sharedPreference=SharedPreference(this)
        val loginButton = findViewById<ImageView>(R.id.business_details_backbutton)
        loginButton.setOnClickListener {
            this.onBackPressed()
        }

        PrintingProductBinding.Selfpickupswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                self_pick="1";
               // showToast(self_pick)
            }else{
                self_pick="0";
              //  showToast(self_pick)
            }
        }
        PrintingProductBinding.insatantswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                insatantDel="1";
              //  showToast(insatantDel)
            }else{
                insatantDel="0";
               // showToast(insatantDel)
            }
        }
        PrintingProductBinding.generaldeliveryswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                GeneralDel="1";
                showToast(GeneralDel)
            }else{
                GeneralDel="0";
                showToast(GeneralDel)
            }
        }
        PrintingProductBinding.returnswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                Return="1";
                showToast(Return)
            }else{
                Return="0";
                showToast(Return)
            }
        }
        PrintingProductBinding.codswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                COD="1";
                showToast(COD)
            }else{
                COD="0";
                showToast(COD)
            }
        }
        PrintingProductBinding.replacementswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                Replacement="1";
                showToast(Replacement)
            }else{
                Replacement="0";
                showToast(Replacement)
            }
        }
        PrintingProductBinding.shopexchangeswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                shopExchange="1";
                showToast(shopExchange)
            }else{
                shopExchange="0";
                showToast(shopExchange)
            }
        }

        PrintingProductBinding.addcolor.setOnClickListener {
            showColorDialog()
        }
        PrintingProductBinding.addmaterial.setOnClickListener {
            showMaterialDialog()
        }
        PrintingProductBinding.addsize.setOnClickListener {
            showSizeDialog()
        }
        PrintingProductBinding.addpattern.setOnClickListener {
            showPatternDialog()
        }
        PrintingProductBinding.addprice.setOnClickListener {
            showPriceDialog()
        }

        PrintingProductBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
        PrintingProductBinding.submitbutton.setOnClickListener {
            PrintingProductBinding.submitbutton.setBackgroundResource(R.drawable.button_loading_background);
            PrintingProductBinding.submitbutton.setEnabled(false)
            Handler().postDelayed({
                PrintingProductBinding.submitbutton.setEnabled(true)
                PrintingProductBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            val delivery_days = PrintingProductBinding.generaldeliverydays.text.toString().trim()

            val gson = Gson()
            val ColorDataJson: String = gson.toJson(CLRData)
            val FabricDataJson: String = gson.toJson(FabriData)
            val PatternDataJson: String = gson.toJson(PatrnData)
            val PriceDataJson: String = gson.toJson(PriceData)
            val SizeDataJson: String = gson.toJson(SizeData)
            val AddonDataJson: String = gson.toJson(ADDONData)
            AddPrintingProductDetails(
                product_id = ItemId.toString().trim(),
                insatantDel = insatantDel.toString().trim(),
                delivery_days= PrintingProductBinding.generaldeliverydays.text.toString().trim(),
                GeneralDel = GeneralDel.toString().trim(),
                self_pick = self_pick.toString().trim(),
                COD = COD.toString().trim(),
                Replacement = Replacement.toString().trim(),
                Return = Return.toString().trim(),
                shopExchange = shopExchange.toString().trim(),
                CLRData = ColorDataJson.toString(),
                FabriData = FabricDataJson.toString().trim(),
                PatrnData = PatternDataJson.toString().trim(),
                PriceData = PriceDataJson.toString().trim(),
                SizeData = SizeDataJson.toString().trim(),
                ADDONData=AddonDataJson.toString().trim()
            )
        }

        MainCategoryList()
        AddonsList()
    }

    internal fun showColorDialog() {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = AddColorLayoutBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        binding.submitbutton.setOnClickListener {


            var CLRObj=ColourData(name = binding.ColorName.text.toString(), code = binding.ColorCode.text.toString());

            CLRData.add(CLRObj)
            colors_show.add(CLRData.last().name+"     "+CLRData.last().code)




            var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                this@AddPrintingProduct,
                android.R.layout.simple_list_item_1,
                colors_show as List<String?>
            )

            // on below line we are setting adapter for our list view.
            PrintingProductBinding.colorNameList.adapter = adapter

            alertDialog.hide()
        }
    }

    internal fun showMaterialDialog() {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = AddFabricMaterialBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        binding.submitbutton.setOnClickListener {


            var FabricObj=FabricData(name = binding.fabricnameet.text.toString(), description = binding.fabricdesct.text.toString());

            FabriData.add(FabricObj)
            fabric_show.add(FabriData.last().name+"\n"+FabriData.last().description)

            var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                this@AddPrintingProduct,
                android.R.layout.simple_list_item_1,
                fabric_show as List<String?>
            )

            // on below line we are setting adapter for our list view.
            PrintingProductBinding.fabricNameList.adapter = adapter
            alertDialog.hide()
        }

    }

    internal fun showSizeDialog() {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = SizeLayoutFileBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)


        binding.submitbutton.setOnClickListener {
            var SizeObj=SizesData(size = binding.nameEt.text.toString(), size_from = binding.sizerangeFromEt.text.toString(),size_to = binding.sizerangeToEt.text.toString());

            SizeData.add(SizeObj)
            size_show.add(SizeData.last().size+"     "+SizeData.last().size_from+"     "+SizeData.last().size_to)




                var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                    this@AddPrintingProduct,
                    android.R.layout.simple_list_item_1,
                    size_show as List<String?>
                )

                // on below line we are setting adapter for our list view.
                PrintingProductBinding.sizesList.adapter = adapter
                alertDialog.hide()
        }
    }

    internal fun showPatternDialog() {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = PatternLayoutFileBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)


        binding.submitbutton.setOnClickListener {

            var PatternObj=PatternData(name = binding.pattrennameet.text.toString(), description = binding.pattrendesct.text.toString());

            PatrnData.add(PatternObj)
            pattern_show.add(PatrnData.last().name+"\n"+PatrnData.last().description)


            var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                this@AddPrintingProduct,
                android.R.layout.simple_list_item_1,
                pattern_show as List<String?>
            )

            // on below line we are setting adapter for our list view.
            PrintingProductBinding.patternList.adapter = adapter

            alertDialog.hide()
        }

    }

    internal fun showPriceDialog() {
        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val binding = PriceLayoutBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)


        binding.submitbutton.setOnClickListener {

            var PriceObj=PricesData(quantity = binding.bundlequantityEt.text.toString(), sale_price = binding.bundlePriceEt.text.toString(),piece_price = binding.priceperpieceEt.text.toString());

            PriceData.add(PriceObj)
            bundle_show.add(PriceData.last().quantity+"     "+PriceData.last().sale_price+"     "+PriceData.last().piece_price)


            var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                this@AddPrintingProduct,
                android.R.layout.simple_list_item_1,
                bundle_show as List<String?>
            )
            // on below line we are setting adapter for our list view.
            PrintingProductBinding.bundleList.adapter = adapter
            alertDialog.hide()
        }

    }

    fun CatalougeProductList(category_id:String){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.CatalougeProductList(sharedPreference.getValueString("token"),category_id)
            requestCall.enqueue(object : Callback<CustomSpinAdapter> {
                override fun onResponse(
                    call: Call<CustomSpinAdapter>,
                    response: Response<CustomSpinAdapter>
                ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
//                                    itemList.clear()
//                                    itemList=response.body();

                                            // Set up the Spinner with the custom adapter
                                            setupSpinner(response.body()!!.maincategories)
//                                if(data.units.isNotEmpty()){
//                                    setupSpinner(data.units)
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

                override fun onFailure(call: Call<CustomSpinAdapter>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }

    internal fun setupSpinner(items:List<Maincategory>){
        spinner = findViewById(R.id.Printingspinnerview)

        val adapter = CustomSpinnerAdapter(this, items)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                ItemId = selectedItem.id
                fetchItemDetailsCal(ItemId)
                // Do something with the selected item (e.g., display its ID or name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

   fun MainCategoryList(){
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
        val binding = SpinneritemlayoutBinding.inflate(layoutInflater)
        val adapter = MainCategoryAdapter(this, items)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                 Maincat_id = selectedItem.id
                fetchItemToSubcategory(Maincat_id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//              //  adapter.filter.filter(newText)
//                return false
//            }
//        })
//        binding.searchEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//           //     adapter.filter.filter(s) // Apply the filter to adapter
//            }
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
    }
     fun fetchItemDetailsCal(itemId: String) {
         try {
             val ordersService = ApiClient.buildService(ApiInterface::class.java)
             val requestCall =
                 ordersService.fetchItemDetails(sharedPreference.getValueString("token"),itemId)
             requestCall.enqueue(object : Callback<ProductCategoryDetails> {
                 override fun onResponse(
                     call: Call<ProductCategoryDetails>,
                     response: Response<ProductCategoryDetails>
                 ) =//dashboardBinding.progressBarLay.visibility  = View.GONE
                     try {
                         when {
                             response.code() == 200 -> {
                                 //data = response.body()!!
                                 if (response.isSuccessful) {
                                     if (response.body() != null) {
                                         if (response.body()!!.error == "0") {
                                             displayItemDetails(response.body()!!.product_details)
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

                 override fun onFailure(call: Call<ProductCategoryDetails>, t: Throwable) {
                     //  dashboardBinding.progressBarLay.visibility  = View.GONE
                     showToast(t.message.toString())
                 }

             })

         } catch (e: Exception) {
             //dashboardBinding.progressBarLay.visibility = View.GONE
             showToast(e.message.toString())
         }
    }
    fun displayItemDetails(itemDetails: ProductDetails) {
        // Example: Update TextViews and ImageView with the fetched details
        PrintingProductBinding.productcategoryet.text = itemDetails.name
        PrintingProductBinding.productsubcategoryet.text = itemDetails.category_name
        Picasso.get().load(itemDetails.image).into(PrintingProductBinding.imageview)
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
                CatalougeProductList(subcat_id)
                            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }
    fun AddonsList(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AddAddonsListDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AddonsListModal>{
                override fun onResponse(
                    call: Call<AddonsListModal>,
                    response: Response<AddonsListModal>
                ) =
                    try {
                        when {
                            response.code() == 200 -> {
                                //data = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body()!=null)
                                    {
                                        if (response.body()!!.error=="0")
                                        {
                                            AddonsSpinner(response.body()!!.add_on_list)
                                        }else{
                                            showToast("Data is Empty")
                                        }

                                    }else{
                                        showToast("Sorry we are unable to catuch data...")
                                    }
                                    // Set up the Spinner with the custom adapter
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
                override fun onFailure(call: Call<AddonsListModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    showToast(t.message.toString())
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    internal fun AddonsSpinner(items:List<AddOn>) {
        spinner = findViewById(R.id.Addonspinnerview)
        val adapter = AddonsListSpinnerAdapter(this, items)
        spinner.adapter = adapter
        val initialSelectedPosition: Int = spinner.getSelectedItemPosition()
        spinner.setSelection(initialSelectedPosition, false);
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedItem = items[position]
                Addons_show.add(selectedItem.name)
                Addons_show.add(selectedItem.description)
                Addons_show.add(selectedItem.price)

                var AddonObj=AddonsData(id =selectedItem.id)
                ADDONData.add(AddonObj)

                var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                    this@AddPrintingProduct,
                    android.R.layout.simple_list_item_1,
                    Addons_show as List<String>
                )
                // on below line we are setting adapter for our list view.
//                ProductBinding.addonsspinnerview.adapter = adapter
                PrintingProductBinding.Addonslistview.adapter = adapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }
    fun AddPrintingProductDetails(
        product_id: String,
        delivery_days: String,
        insatantDel: String,
        GeneralDel:String,
        self_pick:String,
        Return:String,
        COD:String,
        Replacement:String,
        shopExchange:String,
        CLRData:String,
        FabriData:String,
        PatrnData:String,
        PriceData:String,
        SizeData:String,
        ADDONData:String
    ) {

        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AddPrintingProductdetails(sharedPreference.getValueString("token"),"0","0","0","0","0",product_id,insatantDel,delivery_days,GeneralDel,self_pick,COD,Replacement,Return,shopExchange,CLRData,FabriData,PatrnData,PriceData,SizeData,ADDONData)
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

                                            showToast("" + response.body().toString())
//                                    response.body()?.let { showToast(it.message)}
//                                    Response = response.body()!!

//                                    val i = Intent(this@AddPrintingProduct,AddNewProduct::class.java)
//                                    startActivity(i)
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

                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                    showToast(t.message.toString())
                }

            })


        } catch (e: Exception) {
            showToast(e.message.toString())
        }

    }

}