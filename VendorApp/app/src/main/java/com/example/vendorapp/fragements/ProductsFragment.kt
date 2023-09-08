package com.example.vendorapp.fragements


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.activity.AddCatalogueProduct
import com.example.vendorapp.adapters.ProductsAdapter
import com.example.vendorapp.databinding.EditproductdetailslayoutBinding
import com.example.vendorapp.databinding.FragmentProductsBinding
import com.example.vendorapp.databinding.ProductitemdesignBinding
import com.example.vendorapp.modelclass.ProductsModal
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


class ProductsFragment : Fragment() {
    private lateinit var productsBinding: FragmentProductsBinding
     private lateinit var binding: ProductitemdesignBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var productsresponse: ProductsModal
    lateinit var productStatusresponse: Verify_otp_Response
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ProductsAdapter

    var productstatus="0"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sharedPreference=SharedPreference(requireContext())
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreference=SharedPreference(requireContext())
        productsBinding = FragmentProductsBinding.inflate(inflater, container, false)
        binding=ProductitemdesignBinding.inflate(inflater, container, false)

      //  val Product_id=intent.getStringExtra("product_id")


        productsBinding = FragmentProductsBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        productsBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        productsBinding.newordersRequestsViewRecyclerview.hasFixedSize()



        binding.productsstatusswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked==true)
            {
                productstatus="1";
                Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }else{
                productstatus="0";
                Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }

//            ProductStatusDetail(
//                product_status=productstatus.toString().trim(),
//            )
        }
        Productdetails()
        productsBinding.addproductbutton.setBackgroundResource(R.drawable.buttonbackground);
        productsBinding.addproductbutton.setOnClickListener {
            productsBinding.addproductbutton.setBackgroundResource(R.drawable.button_loading_background);
            productsBinding.addproductbutton.setEnabled(false)
            Handler().postDelayed({
                productsBinding.addproductbutton.setEnabled(true)
                productsBinding.addproductbutton.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)
            val intent = Intent (getActivity(), AddCatalogueProduct::class.java)
            getActivity()?.startActivity(intent)
        }
        //return inflater.inflate(R.layout.instantsfragment, container, false)

        return productsBinding.root

    }

    fun Productdetails() {
        try {
            //productsBinding.progressBarLay.visibility= View.VISIBLE
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
                                    if (productsresponse.error == "0") {
                                        if (productsresponse!= null) {
                                        if (productsresponse.products.count() > 0) {
                                            productsBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            productsBinding.noData.visibility = View.GONE
                                            adapter = ProductsAdapter(productsresponse.products,context!!)
                                            //adapter = context?.let { ProductsAdapter(productsresponse.products, context = it)}!!
                                            productsBinding.newordersRequestsViewRecyclerview.adapter = adapter
                                        }
                                        else{
                                        }
                                    } else {
                                        Toast.makeText(context,"List is Empty", Toast.LENGTH_SHORT).show()
                                        productsBinding.newordersRequestsViewRecyclerview.visibility =
                                            View.GONE
                                        productsBinding.noData.visibility = View.VISIBLE
                                    }

                                }
                                else{

                                }
                            }

                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()

                            }

                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<ProductsModal>, t: Throwable) {
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