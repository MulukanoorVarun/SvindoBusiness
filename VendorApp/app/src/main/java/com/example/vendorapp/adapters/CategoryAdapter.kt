package com.example.vendorapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.databinding.CategoryitemdesignBinding
import com.example.vendorapp.modelclass.ProductX
import com.example.vendorapp.modelclass.VendorStatusUpadateModal
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException
class CategoryAdapter (private var productsList: List<ProductX>, private val context: Context): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(private var binding: CategoryitemdesignBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){


        var categorystatus="0"
        private lateinit var sharedPreference: SharedPreference
        private lateinit var categoryStatusresponse: VendorStatusUpadateModal

        @SuppressLint("SetTextI18n")
        fun bind(data: ProductX){
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.productname.text = data.name
            binding.productquanity.text = data.count
            if(data.count!="1"){
                binding.items.text="Items"
            }
            //binding.instock.text = data.count
//            ("₹"+data.net_amount).also { binding.amount.text = it }
            Picasso.get().load(data.image).into(binding.productImage)
            val context = itemView.context

            binding.categoryswitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked == true) {
                    categorystatus = "1"

                 //   Toast.makeText(context, categorystatus, Toast.LENGTH_SHORT).show()
                } else {
                    categorystatus = "0";

                  //  Toast.makeText(context, categorystatus, Toast.LENGTH_SHORT).show()
                }

                sharedPreference=SharedPreference(context)
                try {
                    val ordersService = ApiClient.buildService(ApiInterface::class.java)
                    val requestCall = ordersService.StatusDetails(sharedPreference.getValueString("token"),
                        "product",
                        data.id,
                        categorystatus
                    )
                    requestCall.enqueue(object : Callback<VendorStatusUpadateModal> {
                        override fun onResponse(
                            call: Call<VendorStatusUpadateModal>,
                            response: Response<VendorStatusUpadateModal>
                        ) {
                            try {
                                when {
                                    response.code() == 200 -> {
                                        categoryStatusresponse = response.body()!!
                                        if(categoryStatusresponse.error=="0"){
                                            Toast.makeText(context,categoryStatusresponse.message.toString(), Toast.LENGTH_SHORT).show()
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

                        override fun onFailure(call: Call<VendorStatusUpadateModal>, t: Throwable) {
                            Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding =
            CategoryitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryAdapter.ViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val data = productsList[position]
        holder.bind(data)
    }
}