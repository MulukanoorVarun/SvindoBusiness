package `in`.webgrid.svindobusiness.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.activity.MainActivity
import`in`.webgrid.svindobusiness.databinding.ProductitemdesignBinding
import`in`.webgrid.svindobusiness.fragements.ProductsFragment
import`in`.webgrid.svindobusiness.modelclass.*
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class ProductsAdapter (private var productsList: List<Product>, private val context: Context): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    class ViewHolder(private var binding:ProductitemdesignBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){
        private var dialog:Dialog? = null
        private lateinit var builder: AlertDialog.Builder
        private lateinit var alertDialog: AlertDialog

        var productstatus="0"

        var insatantDel = "0"
        var self_pick = "0"
        var GeneralDel = "0"

        var Return = "0"
        var COD = "0"
        var Replacement = "0"
        var shopExchange = "0"
        var product_id=""


        private lateinit var sharedPreference: SharedPreference
        lateinit var productsresponse: ProductDeatilsResponse
        lateinit var productsEditresponse: Bankdetails_Response
        lateinit var productStatusresponse: VendorStatusUpadateModal


        @SuppressLint("SetTextI18n")
        fun bind(data: Product) {
            binding.productname.text = data.name
          //  binding.productquanity.text = data.quantity
            binding.productunit.text = data.unit
            binding.productcost.text = data.sale_price
            binding.productmrp.text = data.mrp_price
            binding.productstockavalability.text = data.available_stock_count
            Picasso.get().load(data.image).into(binding.productImage)
            val context = itemView.context
            product_id = data.id

            if(data.in_stock=="1"){
               binding.productsstatusswitch.isChecked=true
            }else{
                binding.productsstatusswitch.isChecked =false
            }


            binding.editicon.setOnClickListener {
                dialog = Dialog(context)
                // Set custom dialog layout
                dialog!!.setContentView(R.layout.editproductdetailslayout)
                // Set custom height and width
                dialog!!.window?.setLayout(700, 800)
                // Set transparent background
                // dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Show dialog
                dialog!!.show()
                var mrp_price = dialog!!.findViewById<EditText>(R.id.priceet)
                var sale_price = dialog!!.findViewById<EditText>(R.id.discountpriceet)
                var discountprice= dialog!!.findViewById<TextView>(R.id.discountprice)
                var max_quantity = dialog!!.findViewById<EditText>(R.id.quantityet)
                var min_quantity = dialog!!.findViewById<EditText>(R.id.minquantityet)
                var stock = dialog!!.findViewById<EditText>(R.id.stocket)
                var stocktext = dialog!!.findViewById<TextView>(R.id.stock)
                var generaldeliverydays = dialog!!.findViewById<EditText>(R.id.generaldeliverydays)
                var instantswitch = dialog!!.findViewById<SwitchCompat>(R.id.insatantswitch)
                var Selfpickupswitch = dialog!!.findViewById<SwitchCompat>(R.id.Selfpickupswitch)
                var generaldeliveryswitch = dialog!!.findViewById<SwitchCompat>(R.id.generaldeliveryswitch)
                var returnswitch = dialog!!.findViewById<SwitchCompat>(R.id.returnswitch)
                var codswitch = dialog!!.findViewById<SwitchCompat>(R.id.codswitch)
                var replacementswitch = dialog!!.findViewById<SwitchCompat>(R.id.replacementswitch)
                var shopexchangeswitch = dialog!!.findViewById<SwitchCompat>(R.id.shopexchangeswitch)
                var producteditsubmitbutton= dialog!!.findViewById<AppCompatButton>(R.id.producteditsubmitbutton)


                if(data.is_printable=="1"){
                    mrp_price.isEnabled=false
                   discountprice.text="Price per piece"
                    max_quantity.isEnabled=true
                    min_quantity.isEnabled=true
                }else{
                    mrp_price.isEnabled=true
                    sale_price.setText("Sale Price")
                    max_quantity.isEnabled=false
                    min_quantity.isEnabled=false
                }
                Selfpickupswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        self_pick = "1"
                    } else {
                        self_pick = "0"
                    }
                }

                instantswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        insatantDel = "1"

                    } else {
                        insatantDel = "0"

                    }
                }

                generaldeliveryswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        GeneralDel = "1"
                    } else {
                        GeneralDel = "0"
                    }
                }

               returnswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        Return = "1"
                    } else {
                        Return = "0"
                    }
                }
                codswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        COD = "1"
                    } else {
                        COD = "0"
                    }
                }
              replacementswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        Replacement = "1"
                    } else {
                        Replacement = "0"
                    }
                }
                shopexchangeswitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked == true) {
                        shopExchange = "1"
                    } else {
                        shopExchange = "0"
                    }
                }

                producteditsubmitbutton.setOnClickListener {
                    ProductdetailsEdit(
                        mrp_price=mrp_price.text.toString().trim(),
                        sale_price=sale_price.text.toString().trim(),
                        max_quantity=max_quantity.text.toString().trim(),
                        min_quantity=min_quantity.text.toString().trim(),
                        stock=stock.text.toString().trim(),
                        generaldeliverydays=generaldeliverydays.text.toString().trim(),
                        insatantDel=insatantDel.toString().trim(),
                        self_pick=self_pick.toString().trim(),
                        GeneralDel=GeneralDel.toString().trim(),
                        Return=Return.toString().trim(),
                        COD=COD.toString().trim(),
                        Replacement=Replacement.toString().trim(),
                        shopExchange=shopExchange.toString().trim()
                    )
                    dialog!!.hide()
                }

                sharedPreference= SharedPreference(context)
                try {
                    val ordersService = ApiClient.buildService(ApiInterface::class.java)
                    val requestCall = ordersService.ProductDeatilsresponse(sharedPreference.getValueString("token"),data.id)
                    requestCall.enqueue(object : Callback<ProductDeatilsResponse>{
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<ProductDeatilsResponse>,
                            response: Response<ProductDeatilsResponse>
                        ) {
                            try {
                                when{
                                    response.code() == 200 ->{
                                        Log.d("TAG", data.id.toString())
                                        productsresponse= response.body()!!
                                        if(productsresponse.error=="0") {
                                            if (productsresponse.product_details != null){
                                                mrp_price.setText(productsresponse.product_details.mrp_price)
                                                sale_price.setText(productsresponse.product_details.sale_price)
                                                max_quantity.setText(productsresponse.product_details.max_quantity)
                                                max_quantity.setText(productsresponse.product_details.max_quantity)
                                                min_quantity.setText(productsresponse.product_details.min_quanity)
                                                stock.setText(productsresponse.product_details.available_stock_count)
                                                generaldeliverydays.setText(productsresponse.product_details.delivery_required_days)


                                                if (productsresponse.product_details.instant_delivery == "1") {
                                                    var instant_delivery_enable = true
                                                    instantswitch.isChecked =
                                                        instant_delivery_enable
                                                } else {
                                                    var instant_delivery_disable = false
                                                    instantswitch.isChecked =
                                                        instant_delivery_disable
                                                }


                                                if (productsresponse.product_details.is_printable == "1") {
                                                    stocktext.isVisible=false
                                                    stock.isVisible=false
                                                }else{
                                                    stocktext.isVisible=true
                                                    stock.isVisible=true
                                                }


                                                
                                                if (productsresponse.product_details.self_pickup == "1") {
                                                    var self_pickup_enable = true
                                                    Selfpickupswitch.isChecked = self_pickup_enable
                                                } else {
                                                    var self_pickup_disable = false
                                                    Selfpickupswitch.isChecked = self_pickup_disable
                                                }


                                                if (productsresponse.product_details.general_delivery == "1") {
                                                    var general_delivery_enable = true
                                                    generaldeliveryswitch.isChecked = general_delivery_enable
                                                } else {
                                                    var general_delivery_disable = false
                                                    generaldeliveryswitch.isChecked = general_delivery_disable
                                                }



                                                if (productsresponse.product_details.`return` == "1") {
                                                    var return_enable = true
                                                    returnswitch.isChecked = return_enable
                                                } else {
                                                    var return_disable = false
                                                    returnswitch.isChecked = return_disable
                                                }


                                                if (productsresponse.product_details.cod == "1") {
                                                    var cod_enable = true
                                                    codswitch.isChecked = cod_enable
                                                } else {
                                                    var cod_disable = false
                                                    codswitch.isChecked = cod_disable
                                                }



                                                if (productsresponse.product_details.replacment == "1"){
                                                    var replacment_enable = true
                                                    replacementswitch.isChecked = replacment_enable
                                                } else {
                                                    var replacment_disable = false
                                                    replacementswitch.isChecked = replacment_disable
                                                }



                                                if (productsresponse.product_details.shop_exchange == "1"){
                                                    var shop_exchange_enable = true
                                                    shopexchangeswitch.isChecked = shop_exchange_enable
                                                }else {
                                                    var shop_exchange_disable = false
                                                    shopexchangeswitch.isChecked = shop_exchange_disable
                                                }


                                            } else {
                                               // Toast.makeText(context, productsresponse.message, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    response.code() == 401 -> {
                                        //  Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        //  Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }catch (e: TimeoutException){
                                //Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ProductDeatilsResponse>, t: Throwable){
                            Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }catch (e: Exception){
                    Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }


            binding.deleteicon.setOnClickListener {
                val intent = Intent(context, ProductsFragment::class.java)
                intent.putExtra("product_id", data.id)
                AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes)
                    { _: DialogInterface, _: Int ->
                        sharedPreference= SharedPreference(context)
                        try {
                            val ordersService = ApiClient.buildService(ApiInterface::class.java)
                            val requestCall = ordersService.ProductDelete(sharedPreference.getValueString("token"),data.id)
                            requestCall.enqueue(object : Callback<Bankdetails_Response>{
                                override fun onResponse(
                                    call: Call<Bankdetails_Response>,
                                    response: Response<Bankdetails_Response>
                                ) {
                                    try {
                                        when{
                                            response.code() == 200 -> {
                                                productsEditresponse = response.body()!!
                                                if (productsEditresponse.error == "0") {
                                                    Toast.makeText(context,productsEditresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                                    val intent =
                                                        Intent(context, MainActivity::class.java)
                                                    context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                                }
                                            }
                                            response.code() == 401 -> {
                                                //  Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                            }
                                            else -> {
                                                //  Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }catch (e: TimeoutException){
                                        //Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable){
                                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            })
                        }catch (e: Exception){
                            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
                    .create().show()
             //   Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }

            if(data.mrp_price=="0"){
                binding.productmrp.isVisible = false
                binding.salepricesymbol.isVisible = false
            }
            else{
                binding.productmrp.isVisible = true
                binding.salepricesymbol.isVisible = true
            }


            val sometextview=binding.productmrp
            sometextview.setPaintFlags(sometextview.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

            binding.productsstatusswitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked==true)
                {
                    productstatus="1"
                }else{
                    productstatus="0"
                }
                sharedPreference= SharedPreference(context)
                try {
                    val ordersService = ApiClient.buildService(ApiInterface::class.java)
                    val requestCall = ordersService.StatusDetails(sharedPreference.getValueString("token"),"product",data.id,productstatus)
                    requestCall.enqueue(object : Callback<VendorStatusUpadateModal>{
                        override fun onResponse(
                            call: Call<VendorStatusUpadateModal>,
                            response: Response<VendorStatusUpadateModal>
                        ) {
                            try {
                                when{
                                    response.code() == 200 ->{
                                        productStatusresponse = response.body()!!
                                        if(productStatusresponse.error=="0") {
                                            Toast.makeText(context,productStatusresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    response.code() == 401 -> {
                                        //  Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        //  Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }catch (e: TimeoutException){
                                //Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<VendorStatusUpadateModal>, t: Throwable){
                            Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }catch (e: Exception){
                    Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                }
               // Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }
        }
    fun ProductdetailsEdit(
        mrp_price:String,
        sale_price:String,
        max_quantity:String,
        min_quantity:String,
        stock:String,
        generaldeliverydays:String,
        insatantDel:String,
        self_pick:String,
        GeneralDel:String,
        Return:String,
        COD:String,
        Replacement:String,
        shopExchange:String, ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.ProductDetailsEdit(sharedPreference.getValueString("token"),product_id,sale_price,max_quantity,min_quantity,stock,insatantDel,self_pick,GeneralDel,generaldeliverydays,Return,COD,Replacement,shopExchange,mrp_price)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) =
                    try {
                        when {
                            response.code() == 200 -> {
                                productsEditresponse = response.body()!!
                                if (productsEditresponse != null) {
                                    if (productsEditresponse.error == "0") {
                                        Toast.makeText(context,productsEditresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    } else {
                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                               // showToast(getString(R.string.session_exp))
                            }
                            else -> {
                         //       showToast(getString(R.string.server_error))
                            }
                        }
                    } catch (e: TimeoutException) {
                      //  showToast(getString(R.string.time_out))
                    }

                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsAdapter.ViewHolder(binding, context)
    }
    override fun getItemCount(): Int {

        return productsList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = productsList[position]
        holder.bind(data)
    }
    }
