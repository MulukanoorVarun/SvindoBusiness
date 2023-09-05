package com.example.vendorapp.adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.activity.AcceptOrder
import com.example.vendorapp.activity.MainActivity
import com.example.vendorapp.databinding.BannerlistlayoutdesignBinding
import com.example.vendorapp.databinding.BannerslayoutBinding
import com.example.vendorapp.modelclass.Banner
import com.example.vendorapp.modelclass.BannerX
import com.example.vendorapp.modelclass.BannersListModal
import com.example.vendorapp.modelclass.Verify_otp_Response
import com.example.vendorapp.services.ApiClient
import com.example.vendorapp.services.ApiInterface
import com.example.vendorapp.utils.SharedPreference
import com.example.vendorapp.utils.showToast
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class BannersListAdapter (private var bannersList: List<BannerX>, private val context: Context): RecyclerView.Adapter<BannersListAdapter.ViewHolder>(){
    class ViewHolder(private var binding: BannerlistlayoutdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {
        private var dialog:android.app.Dialog? = null
        lateinit var bannersResponse:BannersListModal
        lateinit var bannerstopResponse:Verify_otp_Response
        private lateinit var sharedPreference: SharedPreference

        fun bind(data: BannerX) {
            Picasso.get().load(data.image).into(binding.BannerImage)
            val context = itemView.context


            binding.cardclicking.setOnClickListener {
                dialog = Dialog(context)
                dialog!!.setContentView(R.layout.bannerviewsclickslayout)
                val textview1 = dialog!!.findViewById<TextView>(R.id.bannerviewsno)
                val textview2 = dialog!!.findViewById<TextView>(R.id.clicksno)
                val button = dialog!!.findViewById<Button>(R.id.deletebtn)
                textview1.text = data.view_count
                textview2.text = data.click_count
                dialog!!.window?.setLayout(700, 600)

                dialog!!.show()
                button.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to stop?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes)
                        { _: DialogInterface, _: Int ->
                            sharedPreference=SharedPreference(context)
                            try {
                                val ordersService = ApiClient.buildService(ApiInterface::class.java)
                                val requestCall = ordersService.BannerDelete(sharedPreference.getValueString("token"),data.id)
                                requestCall.enqueue(object : Callback<Verify_otp_Response>{
                                    override fun onResponse(
                                        call: Call<Verify_otp_Response>,
                                        response: Response<Verify_otp_Response>
                                    ) {
                                        try {
                                            when{
                                                response.code() == 200 ->{
                                                    bannerstopResponse = response.body()!!
                                                    if(bannerstopResponse.error=="0"){
                                                        Toast.makeText(context,bannerstopResponse.message.toString(), Toast.LENGTH_SHORT).show()
                                                    }
//                                                    val intent = Intent(context, MainActivity::class.java)
//                                                    context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                                }
                                                response.code() == 401 -> {
                                                    //  Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                                }
                                                else -> {
                                                    //  Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }catch (e: TimeoutException){
                                            //Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable){
                                        Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }catch (e: Exception){
                                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
                            }

                        }
                        .create().show()
                    dialog!!.hide()
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannersListAdapter.ViewHolder {
        val binding = BannerlistlayoutdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannersListAdapter.ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: BannersListAdapter.ViewHolder, position: Int){
        val data = bannersList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return bannersList.size
    }
}
