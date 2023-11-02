package `in`.webgrid.svindobusiness.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.adapters.GridViewAdapter
import `in`.webgrid.svindobusiness.databinding.ActivitySearchingProductBinding
import `in`.webgrid.svindobusiness.modelclass.ProductXX
import `in`.webgrid.svindobusiness.modelclass.UniversalSearchModal
import `in`.webgrid.svindobusiness.services.ApiClient
import `in`.webgrid.svindobusiness.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class SearchingProductActivity : AppCompatActivity() {
    lateinit var searchingProductBinding: ActivitySearchingProductBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var serachresponse: UniversalSearchModal
     var list: List<ProductXX> = emptyList()
    private lateinit var adapter: GridViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(this)
        searchingProductBinding = ActivitySearchingProductBinding.inflate(layoutInflater)
        setContentView(searchingProductBinding.root)

       // Searchdetails("")
        searchingProductBinding.SearchPage.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Searchdetails(query.toString())
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
              list.filter { it.name.startsWith(newText) }
                return false
            }
        })
    }

    fun Searchdetails(text:String) {
        try {
            val ApiServices = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ApiServices.SearchProducts(sharedPreference.getValueString("token"),text)
            requestCall.enqueue(object : Callback<UniversalSearchModal> {
                override fun onResponse(
                    call: Call<UniversalSearchModal>,
                    response: Response<UniversalSearchModal>
                ) {
                    try {
                        when{
                            response.code() == 200 -> {
                                serachresponse = response.body()!!
                                if (serachresponse.error == "0") {
                                    list = serachresponse.products
                                    adapter = GridViewAdapter(serachresponse.products, this@SearchingProductActivity)
                                    searchingProductBinding.idSearchGRV.adapter = adapter

//                                    // Set the item click listener
//                                    adapter.setOnItemClickListener { products ->
//                                        // Start the new activity and pass the information
//                                        val intent = Intent(this@SearchingProductActivity, ProductDetailsActivity::class.java)
//                                        intent.putExtra("productid", products.id)
//                                        startActivity(intent)
//                                    }

                                }else{
                                    Toast.makeText(applicationContext, "No Match found", Toast.LENGTH_LONG).show()
                                }

                            }
                            response.code() == 401 -> {
                                Toast.makeText(applicationContext, getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(applicationContext, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }


                    }catch (e: TimeoutException){
                        Toast.makeText(applicationContext, getString(R.string.time_out), Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<UniversalSearchModal>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })


        }catch (e: Exception){
            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}