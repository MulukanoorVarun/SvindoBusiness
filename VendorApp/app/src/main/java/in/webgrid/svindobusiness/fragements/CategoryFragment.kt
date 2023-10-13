package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.CategoryAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentCategoryBinding
import`in`.webgrid.svindobusiness.modelclass.ProductCategoryModal
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.activity.NetworkIssueActivity
import `in`.webgrid.svindobusiness.adapters.ProductsAdapter
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
        sharedPreference= SharedPreference(requireContext())
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        sharedPreference= SharedPreference(requireContext())
        categoriesbinding = FragmentCategoryBinding.inflate(inflater, container, false)
        categoriesbinding = FragmentCategoryBinding.inflate(layoutInflater)
        linearLayoutManager = LinearLayoutManager(context)
        categoriesbinding.newordersRequestsViewRecyclerview.layoutManager = linearLayoutManager
        categoriesbinding.newordersRequestsViewRecyclerview.hasFixedSize()
        CategoriesDetails()
        if (checkForInternet(requireContext())) {
            // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }
        return categoriesbinding.root
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

    fun CategoriesDetails() {
        try {
            categoriesbinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.Categoriesdetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<ProductCategoryModal> {
                override fun onResponse(
                    call: Call<ProductCategoryModal>,
                    response: Response<ProductCategoryModal>
                ) {
                    categoriesbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                    categoryresponse = response.body()!!
                                    if (categoryresponse.error == "0") {
                                        if (categoryresponse.products.count() > 0) {
                                            categoriesbinding.newordersRequestsViewRecyclerview.visibility = View.VISIBLE
                                            //adapter = context?.let { CategoryAdapter(categoryresponse.products, it)}!!
                                            adapter = CategoryAdapter(categoryresponse.products, context!!)
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
                    categoriesbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }



}