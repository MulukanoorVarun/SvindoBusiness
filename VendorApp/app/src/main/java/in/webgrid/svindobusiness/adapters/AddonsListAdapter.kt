package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.activity.MainActivity
import`in`.webgrid.svindobusiness.databinding.AddonslistlayoutBinding
import`in`.webgrid.svindobusiness.modelclass.AddOn
import`in`.webgrid.svindobusiness.modelclass.Verify_otp_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class AddonsListAdapter(private var addonsList: List<AddOn>, private val context: Context): RecyclerView.Adapter<AddonsListAdapter.ViewHolder>() {
    class ViewHolder(private var binding: AddonslistlayoutBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var sharedPreference: SharedPreference
        private lateinit var addonsdeleteresponse: Verify_otp_Response
        fun bind(data: AddOn) {
            binding.AddonName.text = data.name?: " "
            binding.AddonDescription.text = data.description?: " "
            binding.AddonPrice.text = data.price?: " "
            Picasso.get().load(data.image).into(binding.Addonimage)
            val context = itemView.context


            binding.deleteicon.setOnClickListener {
                AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes)
                    { _: DialogInterface, _: Int ->
                        sharedPreference= SharedPreference(context)
                        try {
                            val ordersService = ApiClient.buildService(ApiInterface::class.java)
                            val requestCall = ordersService.AddonDelete(sharedPreference.getValueString("token"),data.id)
                            requestCall.enqueue(object : Callback<Verify_otp_Response> {
                                override fun onResponse(
                                    call: Call<Verify_otp_Response>,
                                    response: Response<Verify_otp_Response>
                                ) {
                                    try {
                                        when{
                                            response.code() == 200 ->{
                                                addonsdeleteresponse = response.body()!!
                                                if(addonsdeleteresponse.error=="0") {
                                                    Toast.makeText(context,addonsdeleteresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                                    val intent =
                                                        Intent(context, MainActivity::class.java)
                                                    context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                                }
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
                //   Toast.makeText(context,productstatus, Toast.LENGTH_SHORT).show()
            }
        }
    }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AddonsListAdapter.ViewHolder {
            val binding =
                AddonslistlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AddonsListAdapter.ViewHolder(binding, context)
        }

        override fun getItemCount(): Int {
            return addonsList.size
        }

        override fun onBindViewHolder(holder: AddonsListAdapter.ViewHolder, position: Int) {
            val data = addonsList[position]
            holder.bind(data)
        }
}