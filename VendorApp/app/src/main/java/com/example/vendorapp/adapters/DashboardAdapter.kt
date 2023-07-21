package com.example.vendorapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.activity.AcceptOrder
import com.example.vendorapp.databinding.OrderitemdesignBinding
import com.example.vendorapp.modelclass.Banner
import com.squareup.picasso.Picasso
//
//class DashboardAdapter (private var pendingOrdersList: List<Banner>, private val context: Context): RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {
//    class MyViewHolder(private var binding: OrderitemdesignBinding, private var context: Context?) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: Banner) {
//            "#${data.store_name}".also { binding.storeName.text = it }
//            //binding.orderid.text = data.order_number
//            binding.itemno.text = data.tot_items
//            binding.cost.text = data.amount
//            binding.dayandtime.text = data.delivery_date
////            ("₹"+data.net_amount).also { binding.amount.text = it }
//            Picasso.get().load(data.image_path).into(binding.storeImage)
//            val context = itemView.context
//
//
//            binding.cardclicking.setOnClickListener {
//
//                if (data.order_status == "Order Placed") {
//                    context?.startActivity(
//                        Intent(context, AcceptOrder::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    )
//                }
//            }
//
//        }
////    }
//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): MyViewHolder {
//            val binding =
//                OrderitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return MyViewHolder(binding, context)
//        }
//
//        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            val data = pendingOrdersList[position]
//            holder.bind(data)
//        }
//
//        override fun getItemCount(): Int {
//            return pendingOrdersList.size
//        }
//    }
//

