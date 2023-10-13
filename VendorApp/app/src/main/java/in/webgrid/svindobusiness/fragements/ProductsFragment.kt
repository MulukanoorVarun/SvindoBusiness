package `in`.webgrid.svindobusiness.fragements


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.activity.AddCatalogueProduct
import`in`.webgrid.svindobusiness.adapters.ProductsAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentProductsBinding
import`in`.webgrid.svindobusiness.databinding.ProductitemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.ProductsModal
import`in`.webgrid.svindobusiness.modelclass.Verify_otp_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.activity.NetworkIssueActivity
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
        sharedPreference= SharedPreference(requireContext())
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreference= SharedPreference(requireContext())
        productsBinding = FragmentProductsBinding.inflate(inflater, container, false)
        binding=ProductitemdesignBinding.inflate(inflater, container, false)

      //  val Product_id=intent.getStringExtra("product_id")
        productsBinding = FragmentProductsBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        productsBinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        productsBinding.newordersRequestsViewRecyclerview.hasFixedSize()

        if (checkForInternet(requireContext())) {
            // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }

//        binding.productsstatusswitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked==true)
//            {
//                productstatus="1";
//               // Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
//            }else{
//                productstatus="0";
//              //  Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
//            }

//            ProductStatusDetail(
//                product_status=productstatus.toString().trim(),
//            )

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
    private fun checkForInternet(context: Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun Productdetails() {
        try {
            productsBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.ProductDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductsModal> {
                override fun onResponse(
                    call: Call<ProductsModal>,
                    response: Response<ProductsModal>
                ) {
                    productsBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                productsresponse = response.body()!!
                                if (productsresponse.error == "0") {
                                        if (productsresponse.products.count() > 0) {
                                            productsBinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            productsBinding.noData.visibility = View.GONE
                                            adapter = ProductsAdapter(productsresponse.products, context!!)
                                            //adapter = context?.let { ProductsAdapter(productsresponse.products, context = it)}!!
                                            productsBinding.newordersRequestsViewRecyclerview.adapter =
                                                adapter
                                        } else {
                                        }
                                    } else {
                                        Toast.makeText(context, "List is Empty", Toast.LENGTH_SHORT)
                                            .show()
                                        productsBinding.newordersRequestsViewRecyclerview.visibility =
                                            View.GONE
                                        productsBinding.noData.visibility = View.VISIBLE
                                    }

                                } else {

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
                        Toast.makeText(context, getString(R.string.time_out), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                override fun onFailure(call: Call<ProductsModal>, t: Throwable) {
                    productsBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }



}