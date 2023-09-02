package com.example.vendorapp.fragements

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverypartner.utils.URIPathHelper
import com.example.vendorapp.R
import com.example.vendorapp.activity.AddSpotlightActivity
import com.example.vendorapp.activity.NotificationsActivity
import com.example.vendorapp.adapters.CustomSpinnerAdapter
import com.example.vendorapp.adapters.SpotlightsAdapter
import com.example.vendorapp.databinding.AddonlayoutdesignBinding
import com.example.vendorapp.databinding.AddspotlightlayoutBinding
import com.example.vendorapp.databinding.FragmentSpotlightBinding
import com.example.vendorapp.modelclass.CustomSpinAdapter
import com.example.vendorapp.modelclass.Maincategory
import com.example.vendorapp.modelclass.SpotligtsModal
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.getFileSizeInMB
import com.example.vendorapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeoutException

@SuppressLint("StaticFieldLeak")
private lateinit var spotlightBinding: FragmentSpotlightBinding
class SpotlightFragment : Fragment() {
    private lateinit var sharedPreference: SharedPreference
    lateinit var spotlightresponse: SpotligtsModal
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: SpotlightsAdapter
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var spinner: Spinner
   private lateinit var  binding:AddspotlightlayoutBinding
   var ItemId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(requireContext())

        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreference= SharedPreference(requireContext())
        spotlightBinding = FragmentSpotlightBinding.inflate(inflater, container, false)

        spotlightBinding = FragmentSpotlightBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        spotlightBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        spotlightBinding.newordersRequestsViewRecyclerview.hasFixedSize()

        spotlightBinding.addspotlightbtn.setBackgroundResource(R.drawable.buttonbackground);
        spotlightBinding.addspotlightbtn.setOnClickListener {
            spotlightBinding.addspotlightbtn.setBackgroundResource(R.drawable.button_loading_background);
            spotlightBinding.addspotlightbtn.setEnabled(false)
            Handler().postDelayed({
                spotlightBinding.addspotlightbtn.setEnabled(true)
                spotlightBinding.addspotlightbtn.setBackgroundResource(R.drawable.buttonbackground);
            }, 2000)

            val intent = Intent(getActivity(), AddSpotlightActivity::class.java)
            getActivity()?.startActivity(intent)
        }
//        spotlightBinding.addspotlightbtn.setOnClickListener {
//            showAlertDialog()
//        }


        Spotlightdetails()

        return spotlightBinding.root
    }

//    internal fun showAlertDialog() {
//        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
//        val binding = AddspotlightlayoutBinding.inflate(layoutInflater)
//        builder.setView(binding.root)
//        builder.setCancelable(true)
//        alertDialog = builder.create()
//        alertDialog.show()
//        alertDialog.setCanceledOnTouchOutside(false)
//        ProductsList()
//        binding.submitbutton.setOnClickListener {
//
//            alertDialog.hide()
//        }
//    }

    fun Spotlightdetails() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.SpotlightDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<SpotligtsModal> {
                override fun onResponse(
                    call: Call<SpotligtsModal>,
                    response: Response<SpotligtsModal>
                ) {

                    //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {

                                spotlightresponse = response.body()!!
                                // print(instantResponse)
                                if (spotlightresponse!= null) {
                                    if (spotlightresponse.error == "0") {
                                        if (spotlightresponse.spotlights.isNotEmpty()) {

                                            spotlightBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            adapter = context?.let { SpotlightsAdapter(spotlightresponse.spotlights, context = it) }!!
                                            spotlightBinding.newordersRequestsViewRecyclerview.adapter = adapter

                                        } else {
                                            // Toast.makeText(context,"List is Empty", Toast.LENGTH_SHORT).show()
                                            spotlightBinding.newordersRequestsViewRecyclerview.visibility = View.GONE
                                            spotlightBinding.noData.visibility = View.VISIBLE
                                        }
                                    }
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
                }

                override fun onFailure(call: Call<SpotligtsModal>, t: Throwable) {
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    }
