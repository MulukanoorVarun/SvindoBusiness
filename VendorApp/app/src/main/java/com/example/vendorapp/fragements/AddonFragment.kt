package com.example.vendorapp.fragements

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.activity.bankaccountdetailsbinding
import com.example.vendorapp.adapters.AddonsListAdapter
import com.example.vendorapp.adapters.InstantAdapter
import com.example.vendorapp.databinding.*
import com.example.vendorapp.modelclass.AccountsModal
import com.example.vendorapp.modelclass.AddonsListModal
import com.example.vendorapp.modelclass.Bankdetails_Response
import com.example.vendorapp.modelclass.OrdersListModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
class AddonFragment : Fragment() {

    private lateinit var AddonBinding: FragmentAddonBinding
    private lateinit var popupbinding: AddonpopupdesignBinding

    private lateinit var sharedPreference: SharedPreference
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var addonsresponse: Bankdetails_Response
    private lateinit var addonsListresponse: AddonsListModal
    private lateinit var adapter: AddonsListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popupbinding = AddonpopupdesignBinding.inflate(layoutInflater)
        sharedPreference= SharedPreference(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreference= SharedPreference(requireContext())
        AddonBinding = FragmentAddonBinding.inflate(inflater, container, false)

        AddonBinding = FragmentAddonBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        AddonBinding.AddonsRecyclerview.layoutManager = linearLayoutManager
        AddonBinding.AddonsRecyclerview.hasFixedSize()





        AddonBinding.addonbtn.setOnClickListener {
            showAlertDialog()
        }
        AddAddonsListdetails()

        return AddonBinding.root
    }
    internal fun showAlertDialog() {
        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val binding = AddonlayoutdesignBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        binding.submitbutton.setOnClickListener {
            val addon_name = binding.addonnameEt.text.toString().trim()
            val addon_desc = binding.addondescEt.text.toString().trim()
            val addon_price = binding.addonpriceet.text.toString().trim()
            if (addon_name.isNotEmpty() && addon_desc.isNotEmpty() && addon_price.isNotEmpty()) {
                AddAddonsdetails(
                    addon_name = binding.addonnameEt.text.toString().trim(),
                    addon_desc = binding.addondescEt.text.toString().trim(),
                    addon_price = binding.addonpriceet.text.toString().trim()
                )
            }
            alertDialog.hide()
        }
    }


    fun AddAddonsdetails(
        addon_name:String,
        addon_desc:String,
        addon_price:String,
    ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddAddonsDetails(sharedPreference.getValueString("token"),addon_name,addon_desc,addon_price)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                addonsresponse = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            AddAddonsListdetails()
                                        } else {

                                        }
                                    }else{

                                    }
                                }else{

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


    fun AddAddonsListdetails()
    {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddAddonsListDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AddonsListModal> {
                override fun onResponse(
                    call: Call<AddonsListModal>,
                    response: Response<AddonsListModal>
                ) {
                    try {
                        when {
                            response.code() == 200 -> {
                                    addonsListresponse = response.body()!!
                                    if (addonsListresponse!=null) {
                                    if(addonsListresponse.error=="0")
                                    {
                                        if (addonsListresponse.add_on_list.count()>0) {
                                        AddonBinding.AddonsRecyclerview.visibility = View.VISIBLE
                                        adapter = context?.let { AddonsListAdapter(addonsListresponse.add_on_list, context = it) }!!
                                        AddonBinding.AddonsRecyclerview.adapter = adapter
                                    }
                                    }else{
                                        Toast.makeText(context,(addonsListresponse.message).toString(),Toast.LENGTH_SHORT).show()
                                    }

                                }

                            }

                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp),Toast.LENGTH_SHORT).show()

                            }else -> {
                            Toast.makeText(context,getString(R.string.server_error),Toast.LENGTH_SHORT).show()
                        }
                        }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddonsListModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }
    }





