package com.example.vendorapp.adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.activity.MainActivity
import com.example.vendorapp.databinding.SpotlightitemdeisnBinding
import com.example.vendorapp.modelclass.*
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class SpotlightsAdapter (private var productsList: List<Spotlight>, private val context: Context): RecyclerView.Adapter<SpotlightsAdapter.ViewHolder>()  {
    class ViewHolder(private var binding: SpotlightitemdeisnBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){
        private var dialog:Dialog? = null
        var productstatus="0"
        private lateinit var spotlightboostresponse: Bankdetails_Response
        private lateinit var spotlightStatusresponse: VendorStatusUpadateModal
        private lateinit var sharedPreference: SharedPreference
        fun bind(data: Spotlight) {
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.productname.text = data.name
            binding.productquanity.text = data.quantity
            binding.productunit.text = data.unit
            binding.productcost.text = data.sale_price
            binding.productmrp.text = data.mrp_price
            Picasso.get().load(data.image).into(binding.productImage)
            val context = itemView.context


            if (data.is_boosted=="1"){
                binding.boosticon.setBackgroundResource(R.drawable.booststopicon)
            }
            if (data.is_boosted=="0"){
                binding.boosticon.setBackgroundResource(R.drawable.thundericon)
            }

            binding.deleteicon.setOnClickListener {
                AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes)
                    { _: DialogInterface, _: Int ->
                        sharedPreference=SharedPreference(context)
                        try {
                            val ordersService = ApiClient.buildService(ApiInterface::class.java)
                            val requestCall = ordersService.SpotlightDelete(sharedPreference.getValueString("token"),data.id)
                            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                                override fun onResponse(
                                    call: Call<Bankdetails_Response>,
                                    response: Response<Bankdetails_Response>
                                ) {
                                    try {
                                        when{
                                            response.code() == 200 -> {
                                                spotlightboostresponse= response.body()!!
                                                if (spotlightboostresponse.error == "0") {
                                                    Toast.makeText(context, spotlightboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

                                                }
                                                if (spotlightboostresponse.error == "1") {
                                                    Toast.makeText(context, spotlightboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                                }

                                                if (spotlightboostresponse.error == "2") {
                                                    Toast.makeText(context, spotlightboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
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
            if( data.mrp_price=="0"){
                binding.productmrp.isVisible = false
                binding.salepricesymbol.isVisible = false

            }
            else{
                binding.productmrp.isVisible = true
                binding.salepricesymbol.isVisible = true
            }


            binding.boosticon.setOnClickListener {
                var is_boosted=""
                if(data.is_boosted=="0"){
                    is_boosted="1"
                    dialog = Dialog(context)
                    // Set custom dialog layout
                    dialog!!.setContentView(R.layout.spotlightboostlayout)
                    // Set custom height and width
                    dialog!!.window?.setLayout(700, 700)
                    // Set transparent background
                    //    dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    // Show dialog
                    dialog!!.show()
                    var mrp_price = dialog!!.findViewById<EditText>(R.id.maxamtet)
                    var producteditsubmitbutton= dialog!!.findViewById<AppCompatButton>(R.id.submitbutton)
                    var perviewcost= dialog!!.findViewById<TextView>(R.id.perviewcost)
                    var perclickcost= dialog!!.findViewById<TextView>(R.id.perclickcost)
                    var viewamt= dialog!!.findViewById<TextView>(R.id.viewamt)
                    var perclickcostamt= dialog!!.findViewById<TextView>(R.id.perclickcostamt)

                    viewamt.setText(data.per_view_cost)
                    perclickcostamt.setText(data.per_click_cost)

                    producteditsubmitbutton.setOnClickListener {
//                        AlertDialog.Builder(context)
//                            .setMessage("Are you sure you want to boost?")
//                            .setNegativeButton(android.R.string.no, null)
//                            .setPositiveButton(android.R.string.yes)
//                            { _: DialogInterface, _: Int ->
                             var amount=mrp_price.text.toString().trim()
                                sharedPreference = SharedPreference(context)
                                try {
                                    val ordersService = ApiClient.buildService(ApiInterface::class.java)
                                    val requestCall = ordersService.SpotlightBoosting(sharedPreference.getValueString("token"), data.id, is_boosted,amount)
                                    requestCall.enqueue(object : Callback<Bankdetails_Response>{
                                        override fun onResponse(
                                            call: Call<Bankdetails_Response>,
                                            response: Response<Bankdetails_Response>
                                        ) {
                                            try {
                                                when {
                                                    response.code() == 200 -> {
                                                        spotlightboostresponse= response.body()!!
                                                        if (spotlightboostresponse.error == "0"){
                                                            Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
                                                            val intent = Intent(context, MainActivity::class.java)
                                                            context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                                            dialog!!.hide()
                                                        }
                                                        if (spotlightboostresponse.error == "1") {
                                                            Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
                                                        }
                                                        if (spotlightboostresponse.error == "2") {
                                                            Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                    response.code() == 401 -> {
                                                        //  Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                                    }
                                                    else -> {
                                                        //  Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } catch (e: TimeoutException) {
                                                //Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<Bankdetails_Response>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                context,
                                                t.message.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

//                            }
//                            .create().show()
                    }
                }

                if(data.is_boosted=="1"){
                    is_boosted="0"
//                    Toast.makeText(context,is_boosted.toString(),Toast.LENGTH_SHORT).show()
//                    AlertDialog.Builder(context)
//                        .setMessage("Are you sure you want to boost?")
//                        .setNegativeButton(android.R.string.no, null)
//                        .setPositiveButton(android.R.string.yes)
//                        { _: DialogInterface, _: Int ->
                            sharedPreference=SharedPreference(context)
                            try {
                                val ordersService = ApiClient.buildService(ApiInterface::class.java)
                                val requestCall = ordersService.SpotlightBoosting(sharedPreference.getValueString("token"),data.id,is_boosted,"0")
                                requestCall.enqueue(object : Callback<Bankdetails_Response>{
                                    override fun onResponse(
                                        call: Call<Bankdetails_Response>,
                                        response: Response<Bankdetails_Response>
                                    ) {
                                        try {
                                            when{
                                                response.code() == 200 ->{
                                                    spotlightboostresponse= response.body()!!
                                                    if(spotlightboostresponse.error=="0") {
                                                        Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
                                                        val intent = Intent(context, MainActivity::class.java)
                                                        context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                                    }
                                                    if(spotlightboostresponse.error=="1") {
                                                        Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
                                                    }
                                                    if(spotlightboostresponse.error=="2") {
                                                        Toast.makeText(context, spotlightboostresponse.message.toString (),Toast.LENGTH_SHORT).show()
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
//                        .create().show()
//                }

                //   Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }



            val sometextview=binding.productmrp
            sometextview.setPaintFlags(sometextview.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

            binding.productsstatusswitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked==true)
                {
                    productstatus="1"
                }else{
                    productstatus="0";
                }

                sharedPreference=SharedPreference(context)
                try {
                    val ordersService = ApiClient.buildService(ApiInterface::class.java)
                    val requestCall = ordersService.StatusDetails(sharedPreference.getValueString("token"),"spotlight",data.id,productstatus)
                    requestCall.enqueue(object : Callback<VendorStatusUpadateModal> {
                        override fun onResponse(
                            call: Call<VendorStatusUpadateModal>,
                            response: Response<VendorStatusUpadateModal>
                        ) {
                            try {
                                when{
                                    response.code() == 200 ->{
                                        spotlightStatusresponse = response.body()!!
                                        if(spotlightStatusresponse.error=="0"){
                                            Toast.makeText(context,spotlightStatusresponse.message.toString(), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()

            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotlightsAdapter.ViewHolder {
        val binding =
            SpotlightitemdeisnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpotlightsAdapter.ViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
    override fun onBindViewHolder(holder: SpotlightsAdapter.ViewHolder, position: Int) {
        val data = productsList[position]
        holder.bind(data)
    }
}