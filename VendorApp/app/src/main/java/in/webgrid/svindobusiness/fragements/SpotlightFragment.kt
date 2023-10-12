package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.activity.AddSpotlightActivity
import`in`.webgrid.svindobusiness.adapters.SpotlightsAdapter
import`in`.webgrid.svindobusiness.databinding.AddspotlightlayoutBinding
import`in`.webgrid.svindobusiness.databinding.FragmentSpotlightBinding
import`in`.webgrid.svindobusiness.modelclass.SpotligtsModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        spotlightBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.SpotlightDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<SpotligtsModal> {
                override fun onResponse(
                    call: Call<SpotligtsModal>,
                    response: Response<SpotligtsModal>
                ) {
                    spotlightBinding.progressBarLay.progressBarLayout.visibility = View.GONE
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
                    spotlightBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    }
