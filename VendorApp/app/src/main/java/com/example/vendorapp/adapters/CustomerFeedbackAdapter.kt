package com.example.vendorapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.activity.AcceptOrder
import com.example.vendorapp.databinding.CustomerfeedbackitemdesignBinding
import com.example.vendorapp.databinding.OrderitemdesignBinding
import com.example.vendorapp.modelclass.Data
import com.example.vendorapp.modelclass.OrderXXX
import com.squareup.picasso.Picasso

class CustomerFeedbackAdapter (private var feedbackList: List<Data>, private val context: Context): RecyclerView.Adapter<CustomerFeedbackAdapter.ViewHolder>() {
    class ViewHolder(private var binding: CustomerfeedbackitemdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(data: Data) {
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.productname.text = data.name
            binding.feedbackdesc.text = data.comment
//            ("₹"+data.net_amount).also { binding.amount.text = it }
            Picasso.get().load(data.image).into(binding.productImage)
             context = itemView.context

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            CustomerfeedbackitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = feedbackList[position]
        holder.bind(data)
    }
    override fun getItemCount(): Int {
        return feedbackList.size
    }
}