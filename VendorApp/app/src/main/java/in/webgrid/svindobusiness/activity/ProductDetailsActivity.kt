package `in`.webgrid.svindobusiness.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Html
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.adapters.CarouselAdapter
import `in`.webgrid.svindobusiness.adapters.GridViewAdapter
import `in`.webgrid.svindobusiness.databinding.ActivityProductDetailsBinding
import `in`.webgrid.svindobusiness.databinding.ActivitySearchingProductBinding
import `in`.webgrid.svindobusiness.modelclass.CarouselItem
import `in`.webgrid.svindobusiness.modelclass.Image
import `in`.webgrid.svindobusiness.modelclass.ProductDetailsModal
import `in`.webgrid.svindobusiness.modelclass.UniversalSearchModal
import `in`.webgrid.svindobusiness.services.ApiClient
import `in`.webgrid.svindobusiness.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var productDetailsBinding: ActivityProductDetailsBinding
    lateinit var productresponse: ProductDetailsModal
    lateinit var progress: ProgressDialog
    private lateinit var sharedPreference: SharedPreference
    lateinit var adapter:CarouselAdapter
    private val carouselItems =ArrayList<String>()

    internal val viewPager: ViewPager2 by lazy { findViewById(R.id.viewPager) }
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    
    var image_path=""
    var product_name=""
    var is_printing=""
    var product_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsBinding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(productDetailsBinding.root)
        sharedPreference= SharedPreference(this)

        progress = ProgressDialog(this,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)

        productDetailsBinding.backbutton.setOnClickListener {
            val i = Intent(this@ProductDetailsActivity,SearchingProductActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }

        productDetailsBinding.addProduct.setOnClickListener {
            val intent = Intent(this, AddCatalogueProduct::class.java)
            intent.putExtra("image_path",image_path)
            intent.putExtra("product_name",product_name)
            intent.putExtra("is_printing",is_printing)
            intent.putExtra("product_id",product_id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        val i = intent
        product_id = i.getStringExtra("product_id").toString()

        Productdetails(product_id.toString())
    }

    fun Productdetails(id:String) {
        progress.show()
        try {
            val ApiServices = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ApiServices.ProductDetails(sharedPreference.getValueString("token"),id)
            requestCall.enqueue(object : Callback<ProductDetailsModal> {
                override fun onResponse(
                    call: Call<ProductDetailsModal>,
                    response: Response<ProductDetailsModal>
                ) {
                    progress.hide()
                    try {
                        when{
                            response.code() == 200 -> {
                                productresponse = response.body()!!
                                if (productresponse != null) {
                                    if (productresponse.error == "0") {
                                        progress.hide()
                                        productDetailsBinding.productname.text=productresponse.details.name
                                        product_name=productresponse.details.name
                                        is_printing=productresponse.details.is_printing
                                        productDetailsBinding.descTxt.text= Html.fromHtml(productresponse.details.description).toString()
                                        adapter=CarouselAdapter(productresponse.details.images)
                                        viewPager.adapter = adapter

                                        // Automatically scroll through the images every X milliseconds
                                        val autoScrollRunnable = object : Runnable {
                                            override fun run() {
                                                if (currentPage == carouselItems.size) {
                                                    currentPage = 0
                                                }
                                                viewPager.setCurrentItem(currentPage++, true)
                                                handler.postDelayed(this, 4000) // Adjust the delay as needed (in milliseconds)
                                            }
                                        }
                                        handler.post(autoScrollRunnable)

                                        if (productresponse.details.images.size > 0) {
                                            for (i in productresponse.details.images){
                                                image_path= productresponse.details.images[0].image.toString()
                                                carouselItems!!.add(i.image)
                                            }
                                        }

                                    } else {
                                        progress.hide()
                                        Toast.makeText(applicationContext, "No Match found", Toast.LENGTH_LONG).show()
                                    }

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

                override fun onFailure(call: Call<ProductDetailsModal>, t: Throwable) {
                    progress.hide()
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e: Exception){
            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}