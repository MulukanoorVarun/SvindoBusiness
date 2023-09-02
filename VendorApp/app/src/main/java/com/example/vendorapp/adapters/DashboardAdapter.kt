package com.example.vendorapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.activity.AcceptOrder
import com.example.vendorapp.databinding.OrderitemdesignBinding
import com.example.vendorapp.modelclass.Banner
import com.example.vendorapp.modelclass.Order
import com.example.vendorapp.modelclass.OrderXX
import com.example.vendorapp.modelclass.OrderXXX
import com.squareup.picasso.Picasso


class DashboardAdapter (private var pendingOrdersList: List<OrderXXX>, private val context: Context): RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){
    class ViewHolder(private var binding: OrderitemdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: OrderXXX) {
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.orderid.text = data.order_number
            binding.itemno.text = data.tot_items
            binding.cost.text = data.amount
            binding.dayandtime.text = data.delivery_date
            if(data.tot_items!="1"){
                binding.item.text="Items"
            }
//            ("₹"+data.net_amount).also { binding.amount.text = it }
            Picasso.get().load(data.image_path).into(binding.storeImage)
            val context = itemView.context


            binding.cardclicking.setOnClickListener {
                val intent = Intent(context, AcceptOrder::class.java)
                intent.putExtra("order_id",  data.id)
                    context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }


        }
 }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding =
                OrderitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding, context)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = pendingOrdersList[position]
            holder.bind(data)
        }
        override fun getItemCount(): Int {
            return pendingOrdersList.size
        }
    }


