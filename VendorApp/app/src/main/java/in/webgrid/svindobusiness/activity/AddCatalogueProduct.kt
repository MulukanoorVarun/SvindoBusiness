package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.adapters.*
import `in`.webgrid.svindobusiness.databinding.SpinneritemdesignBinding
import `in`.webgrid.svindobusiness.databinding.SpinneritemlayoutBinding
import `in`.webgrid.svindobusiness.modelclass.*
import `in`.webgrid.svindobusiness.services.ApiClient
import `in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.Utils.showToast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import `in`.webgrid.svindobusiness.databinding.ActivityAddCatalogueProductBinding
import `in`.webgrid.svindobusiness.modelclass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var ProductBinding: ActivityAddCatalogueProductBinding

class AddCatalogueProduct : AppCompatActivity() {
    private lateinit var productdetailsResponse: Bankdetails_Response
    private lateinit var cataloguelistResponse: CustomSpinAdapter
    private lateinit var unitslistResponse: UnitModal
    private lateinit var addondata: AddonsListModal
    private lateinit var maincategoryResponse: MainCategoryModal
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter:AdonsRecyclerviewadapter
    private var dialog:Dialog? = null
    private lateinit var sharedPreference: SharedPreference
    // Inside your Activity or Fragment
    lateinit var productsarrayList: ArrayList<String>
    lateinit var addondsarrayList: ArrayList<String>
    lateinit var progress: ProgressDialog

    private lateinit var spinner: Spinner
    private var itemList: MutableList<String> =  mutableListOf()

    var itemId = ""
    var unitId = ""
    var Maincat_id = ""
    var subcat_id = ""
    var size_id = ""

    var insatantDel = "0"
    var self_pick = "0"
    var GeneralDel = "0"

    var is_printing = "0"


    var Return = "0"
    var COD = "0"
    var Replacement = "0"
    var shopExchange = "0"

    var ADDONData = ArrayList<AddonsData>()
    var Addons_show = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsarrayList = ArrayList()
        addondsarrayList = ArrayList()
        setContentView(R.layout.activity_add_catalogue_product)
        ProductBinding = ActivityAddCatalogueProductBinding.inflate(layoutInflater)
        setContentView(ProductBinding.root)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)

        val i = intent
        var image_path = i.getStringExtra("image_path")
       // showToast(image_path.toString())
        var product_name = i.getStringExtra("product_name")
        var is_printing= i.getStringExtra("is_printing")

//        if(is_printing=="1"){
//            ProductBinding.printingswitch.isChecked=true
//        }else{
//            ProductBinding.printingswitch.isChecked=false
//        }
        Picasso.get().load(image_path).into(ProductBinding.imageview)
        ProductBinding.testView.text=product_name

//        linearLayoutManager = LinearLayoutManager(this)
//        ProductBinding.AddonsRecyclerView.layoutManager = linearLayoutManager
//        ProductBinding.AddonsRecyclerView.hasFixedSize()


        sharedPreference = SharedPreference(this)
        val loginButton = findViewById<ImageView>(R.id.business_details_backbutton)
        loginButton.setOnClickListener {
            this.onBackPressed()
        }
        ProductBinding.Selfpickupswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                self_pick = "1";
             //   showToast(self_pick)
            } else {
                self_pick = "0";
               // showToast(self_pick)
            }
        }
        ProductBinding.insatantswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                insatantDel = "1";
              //  showToast(insatantDel)
            } else {
                insatantDel = "0";
                //showToast(insatantDel)
            }
        }
        ProductBinding.generaldeliveryswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                GeneralDel = "1";
              //  showToast(GeneralDel)
            } else {
                GeneralDel = "0";
              //  showToast(GeneralDel)
            }
        }
        ProductBinding.returnswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                Return = "1"
              //  showToast(Return)
            } else {
                Return = "0"
               // showToast(Return)
            }
        }
        ProductBinding.codswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                COD = "1"
               // showToast(COD)
            } else {
                COD = "0"
             //   showToast(COD)
            }
        }
        ProductBinding.replacementswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                Replacement = "1";
               // showToast(Replacement)
            } else {
                Replacement = "0";
                //showToast(Replacement)
            }
        }
        ProductBinding.shopexchangeswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                shopExchange = "1";
               // showToast(shopExchange)
            } else {
                shopExchange = "0";
              //  showToast(shopExchange)
            }
        }

        ProductBinding.Addonslistview.setScrollContainer(false)
        ProductBinding.Addonslistview.setVerticalScrollBarEnabled(false)

        ProductBinding.printingswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                is_printing = "1"
                ProductBinding.discountprice.text="Price per piece"

                ProductBinding.priceet.isEnabled = false
               // ProductBinding.unitsspinner.isEnabled = false
                ProductBinding.stocket.isEnabled = false
                ProductBinding.quantityet.isEnabled = true
                ProductBinding.minquantityet.isEnabled = true
                ProductBinding.Addaddon.isEnabled = true

               // showToast(is_printing)
            }
            else if(isChecked == false){
                is_printing = "0"
                ProductBinding.discountprice.text="Sale Price"
                ProductBinding.priceet.isEnabled = true
             //   ProductBinding.unitsspinner.isEnabled = true
                ProductBinding.stocket.isEnabled = true
                ProductBinding.quantityet.isEnabled = false
                ProductBinding.minquantityet.isEnabled = false
                ProductBinding.Addaddon.isEnabled = false
              //  showToast(is_printing)
            }
        }

        if( ProductBinding.printingswitch.isChecked==false){
            ProductBinding.quantityet.isEnabled = false
            ProductBinding.minquantityet.isEnabled = false
            ProductBinding.Addaddon.isEnabled = false
        }else{
            ProductBinding.quantityet.isEnabled = true
            ProductBinding.minquantityet.isEnabled = true
            ProductBinding.Addaddon.isEnabled = true
        }

        ProductBinding.addbtn.setOnClickListener {
            val intent = Intent(this, AddNewProduct::class.java)
            startActivity(intent)
        }
//
//        ProductBinding.addaddons.setOnClickListener {
//            showAddonDialog()
//        }
        ProductBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
        ProductBinding.submitbutton.setOnClickListener {
            ProductBinding.submitbutton.setBackgroundResource(R.drawable.button_loading_background);
            ProductBinding.submitbutton.setEnabled(false)
            Handler().postDelayed({
               ProductBinding.submitbutton.setEnabled(true)
                ProductBinding.submitbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            val deliverydays = ProductBinding.generaldeliverydays.text.toString().trim()
            val mrp_price = ProductBinding.priceet.text.toString().trim()
            val sale_price = ProductBinding.discountpriceet.text.toString().trim()
            val stock = ProductBinding.stocket.text.toString().trim()
            val quantity = ProductBinding.quantityet.text.toString().trim()
            val minQuantity=ProductBinding.minquantityet.text.toString().trim()

            val gson = Gson()
            val AddonDataJson: String = gson.toJson(ADDONData)

            Productdetails(
                product_id = itemId.toString().trim(),
                unit_id = unitId.toString().trim(),
                size_id = size_id.toString().trim(),
                quantity = ProductBinding.quantityet.text.toString().trim(),
                minQuantity=ProductBinding.minquantityet.text.toString().trim(),
                insatantDel = insatantDel.toString().trim(),
                printing = is_printing.toString().trim(),
                delivery_days = ProductBinding.generaldeliverydays.text.toString().trim(),
                GeneralDel = GeneralDel.toString().trim(),
                self_pick = self_pick.toString().trim(),
                COD = COD.toString().trim(),
                Replacement = Replacement.toString().trim(),
                Return = Return.toString().trim(),
                shopExchange = shopExchange.toString().trim(),
                mrp_price = ProductBinding.priceet.text.toString().trim(),
                sale_price = ProductBinding.discountpriceet.text.toString().trim(),
                stock = ProductBinding.stocket.text.toString().trim(),
                ADDONData =AddonDataJson.toString().trim()
            )
        }

//        ProductBinding.addbtn.setOnClickListener {
//            dialog = Dialog(this@AddCatalogueProduct)
//
//            // Set custom dialog layout
//            dialog!!.setContentView(R.layout.searchablespinnerlayout)
//
//            // Set custom height and width
//            dialog!!.window?.setLayout(800, 800)
//
//            // Set transparent background
//            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            // Show dialog
//            dialog!!.show()
//            // Initialize variables from dialog
//            val editText = dialog!!.findViewById<EditText>(R.id.edit_text)
//            val listView = dialog!!.findViewById<ListView>(R.id.list_view)
//           // AddonsList()
//            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
//                this@AddCatalogueProduct,
//                android.R.layout.simple_list_item_1, items
//            )
//            // Set adapter
//            listView.adapter = adapter
//
//            editText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    adapter.filter.filter(s)
//                }
//                override fun afterTextChanged(s: Editable?) {}
//            })
//        }

        Unitsdetails()
        AddonsList()
        //MainCategoryList()
    }

//    internal fun showAddonDialog() {
//        builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
//        val binding =AddonlayoutdesignBinding.inflate(layoutInflater)
//        builder.setView(binding.root)
//        builder.setCancelable(true)
//        alertDialog = builder.create()
//        alertDialog.show()
//        alertDialog.setCanceledOnTouchOutside(false)
//
//        binding.submitbutton.setOnClickListener {
//
//
////            var AddonObj=ColourData(name = binding.ColorName.text.toString(), code = binding.ColorCode.text.toString());
////
////            ADDONData.add(AddonObj)
////            Addons_show.add(ADDONData.last().name+"     "+ADDONData.last().code)
//
//
//
//
//            var adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
//                this@AddCatalogueProduct,
//                android.R.layout.simple_list_item_1,
//                Addons_show as List<String?>
//            )
//
//            // on below line we are setting adapter for our list view.
//            ProductBinding.colorNameList.adapter = adapter
//
//            alertDialog.hide()
//        }
//    }


    fun Productdetails(
        product_id: String,
        unit_id: String,
        size_id:String,
        delivery_days: String,
        insatantDel: String,
        GeneralDel: String,
        self_pick: String,
        Return: String,
        COD: String,
        Replacement: String,
        shopExchange: String,
        mrp_price: String,
        sale_price: String,
        stock: String,
        ADDONData: String,
        quantity: String,
        printing: String,
        minQuantity:String,
    ) {
        progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.CatProductDetails(sharedPreference.getValueString("token"), product_id, "0", mrp_price, sale_price, quantity, unit_id, stock, insatantDel, delivery_days, GeneralDel, self_pick, COD, Replacement, Return, shopExchange, ADDONData, printing, minQuantity,size_id)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) {
                    progress.dismiss()
                try
                {
                    when {
                        response.code() == 200 -> {
                            productdetailsResponse = response.body()!!
                            if (productdetailsResponse != null) {
                                if (productdetailsResponse.error == "0") {
                                    showToast(productdetailsResponse.message.toString())
                                    val i = Intent(this@AddCatalogueProduct, MainActivity::class.java)
                                    startActivity(i)
                                    progress.dismiss()
                                } else {
                                    progress.dismiss()
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
                } catch (e: TimeoutException)
                {
                    showToast(getString(R.string.time_out))
                }
            }

                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                    ProductBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    progress.dismiss()
                    showToast(t.message.toString())
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            showToast(e.message.toString())
        }
    }
    fun CatalougeProductList(category_id:String) {
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
                                cataloguelistResponse = response.body()!!
                                if (cataloguelistResponse.error=="0") {

                                    // Set up the Spinner with the custom adapter
                                    productsarrayList=ArrayList()
                                    if(cataloguelistResponse.maincategories.size>0)
                                    {
                                        for (i in cataloguelistResponse.maincategories)
                                        {
                                            productsarrayList!!.add(i.name)
                                        }
                                        setupSpinner(productsarrayList!!)
                                    } else {

                                    }

//                                if(data.units.isNotEmpty()){
//                                    setupSpinner(data.units)
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


    internal fun setupSpinner(items:ArrayList<String>) {
        val textview = findViewById<TextView>(R.id.testView)
        val imageview = findViewById<ImageView>(R.id.imageview)

        ProductBinding.testView.setOnClickListener {
            // Initialize dialog
            dialog = Dialog(this@AddCatalogueProduct)

            // Set custom dialog layout
            dialog!!.setContentView(R.layout.searchablespinnerlayout)

            // Set custom height and width
            dialog!!.window?.setLayout(800, 800)

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
                this@AddCatalogueProduct,
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
                for (i in cataloguelistResponse.maincategories)
                {
                    if(i.name==textview.text)
                    {
                        itemId=i.id
                        Picasso.get().load(i.image).into(imageview)
                       // showToast(itemId.toString())
                    }
                }
                dialog!!.dismiss()
            }
        }
    }




//        spinner = findViewById(R.id.spinnerview)
//        val adapter = CustomSpinnerAdapter(this, items)
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
//                val selectedItem = items[position]
//                itemId = selectedItem.id
//                // showToast(itemId.toString())
//
//                //   fetchItemDetailsCal(itemId)
//                // Do something with the selected item (e.g., display its ID or name)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing when nothing is selected
//            }
//        }


    fun Unitsdetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.Unitdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<UnitModal> {
                override fun onResponse(
                    call: Call<UnitModal>,
                    response: Response<UnitModal>
                ) =
                    try {
                        when {
                            response.code() == 200 -> {
                                unitslistResponse = response.body()!!
                                if (unitslistResponse.error == "0") {
                                    if (response.isSuccessful) {
                                        setSpinner(unitslistResponse.units)
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

    internal fun setSpinner(items: List<Unititem>) {
        spinner = findViewById(R.id.unitsspinner)
        var binding=SpinneritemlayoutBinding.inflate(layoutInflater)
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
                unitId = selectedItem.id
                //  fetchItemDetailsCal(itemId)
                // Do something with the selected item (e.g., display its ID or name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun AddonsList() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddAddonsListDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AddonsListModal> {
                override fun onResponse(
                    call: Call<AddonsListModal>,
                    response: Response<AddonsListModal>
                ) =
                    try {
                        when {
                            response.code() == 200 -> {
                                 addondata = response.body()!!
                                if (response.isSuccessful) {
                                    if (addondata.error == "0") {
//                                            AddonsSpinner(response.body()!!.add_on_list)
//                                        } else {
//                                          //  showToast("Data is Empty")
//                                        }

                                        addondsarrayList = ArrayList()
                                        if (addondata.add_on_list.size > 0) {
                                            for (i in addondata.add_on_list){
                                                addondsarrayList!!.add(i.name)
                                            }
                                            AddonsSpinner(addondsarrayList!!)
                                        } else {

                                        }
                                    } else {
                                        // showToast("Sorry we are unable to catuch data...")
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

    internal fun AddonsSpinner(items:ArrayList<String>) {
        var Listview = findViewById<ListView>(R.id.Addonslistview)

        ProductBinding.Addaddon.setOnClickListener {
            // Initialize dialog
            dialog = Dialog(this@AddCatalogueProduct)

            // Set custom dialog layout
            dialog!!.setContentView(R.layout.searchablespinnerlayout)

            // Set custom height and width
            dialog!!.window?.setLayout(800, 800)

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
                this@AddCatalogueProduct,
                android.R.layout.simple_list_item_1, items
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
               var selectedItem=adapter.getItem(position).toString()

                for (i in addondata.add_on_list)
                {
                    if(i.name==selectedItem)
                    {
                        var addonId=i.id
                        var AddonObj = AddonsData(addonId)
                      //  showToast(AddonObj.toString())
                        ADDONData.add(AddonObj)
                    }
                }

                itemList.add(selectedItem)
                val selectedItemsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemList)
                Listview.adapter = selectedItemsAdapter
                selectedItemsAdapter.notifyDataSetChanged()
                dialog!!.dismiss()

                Listview.setOnItemClickListener { _, _, position, _ ->
                    // Remove the selected item from the list
                    itemList.removeAt(position)
                    ADDONData.removeAt(position)
                    // Notify the adapter that the data set has changed
                    selectedItemsAdapter.notifyDataSetChanged()
                }
            }
//            Listview.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                      val selectedItem = items[position]
//                    Addons_show.add(selectedItem.name)
//                    Addons_show.add(selectedItem.description)
//                    Addons_show.add(selectedItem.price)
//                    var AddonObj = AddonsData(id = selectedItem.id)
//                    ADDONData.add(AddonObj)
//
//                    val customAdapter = CustomAdapter(this@AddCatalogueProduct, Addons_show)
//                    ProductBinding.Addonslistview.adapter = customAdapter
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    // Do nothing when nothing is selected
//                }
//            }
        }
    }
//       // spinner = findViewById(R.id.addonsspinnerview)
//        val adapter = AddonsListSpinnerAdapter(this, items)
//        spinner.adapter = adapter
//        spinner.setSelection(0,false)
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val selectedItem = items[position]
//                Addons_show.add(selectedItem.name)
//                Addons_show.add(selectedItem.description)
//                Addons_show.add(selectedItem.price)
//                var AddonObj = AddonsData(id = selectedItem.id)
//                ADDONData.add(AddonObj)
//
//                val customAdapter = CustomAdapter(this@AddCatalogueProduct,Addons_show)
//                ProductBinding.Addonslistview.adapter = customAdapter
//
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing when nothing is selected
//            }
//        }


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
                                maincategoryResponse = response.body()!!
                                if (maincategoryResponse.error == "0") {
                                    if (response.isSuccessful) {
                                        MaincategorySpinner(response.body()!!.maincategories)
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
        val binding = SpinneritemdesignBinding.inflate(layoutInflater)
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
                Maincat_id = selectedItem.id
                fetchItemToSubcategory(Maincat_id)
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
                CatalougeProductList(subcat_id)
              //  SizesList(subcat_id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

//    fun SizesList(subcat_id:String){
//        try {
//            val ordersService = ApiClient.buildService(ApiInterface::class.java)
//            val requestCall =
//                ordersService.SubCategoryDetails(sharedPreference.getValueString("token"),subcat_id)
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
//                                    if (response.body() != null) {
//                                        if (response.body()!!.error == "0") {
//                                            SizesSpinner(response.body()!!.sizes)
//                                        } else {
//
//                                        }
//                                    }else{
//
//                                    }
//                                }else{
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
//        } catch (e: Exception) {
//            //dashboardBinding.progressBarLay.visibility = View.GONE
//            showToast(e.message.toString())
//        }
//    }
//    internal fun SizesSpinner(items: List<Sizes>) {
//        spinner = findViewById(R.id.Sizesspinnerview)
//
//        val adapter = SizesAdapter(this, items)
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
//                val selectedItem = items[position]
//                size_id = selectedItem.id
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing when nothing is selected
//            }
//        }
//    }

}