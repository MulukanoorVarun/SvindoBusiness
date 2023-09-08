package com.example.vendorapp.fragements

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.AddonsListAdapter
import com.example.vendorapp.adapters.CategoryAdapter
import com.example.vendorapp.adapters.ProductsAdapter
import com.example.vendorapp.databinding.FragmentCategoryBinding
import com.example.vendorapp.modelclass.ProductCategoryModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var categoriesbinding: FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var categoryresponse: ProductCategoryModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference=SharedPreference(requireContext())
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        sharedPreference=SharedPreference(requireContext())
        categoriesbinding = FragmentCategoryBinding.inflate(inflater, container, false)
        categoriesbinding = FragmentCategoryBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        categoriesbinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        categoriesbinding.newordersRequestsViewRecyclerview.hasFixedSize()
        CategoriesDetails()
        return categoriesbinding.root
        }
    fun CategoriesDetails() {
        try {
            //dashboardBinding.progressBarLay.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.CategoriesDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductCategoryModal> {
                override fun onResponse(
                    call: Call<ProductCategoryModal>,
                    response: Response<ProductCategoryModal>
                ) {
                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                    categoryresponse = response.body()!!
                                    if (categoryresponse.error == "0") {
                                        if (categoryresponse.products.count() > 0) {
                                            categoriesbinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            adapter = context?.let { CategoryAdapter(categoryresponse.products, it)}!!
                                          //  adapter = context?.let { CategoryAdapter(categoryresponse.products, context = it)}!!
                                            categoriesbinding.newordersRequestsViewRecyclerview.adapter = adapter
                                        }
                                    } else {
                                      // Toast.makeText(context,"",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                                response.code() == 401 -> {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.session_exp),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                                else -> {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.server_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProductCategoryModal>, t: Throwable) {
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