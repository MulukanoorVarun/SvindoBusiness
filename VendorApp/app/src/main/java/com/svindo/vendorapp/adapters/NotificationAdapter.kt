package com.svindo.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.svindo.vendorapp.databinding.NotificationspartdesignBinding
import com.svindo.vendorapp.modelclass.XX
import com.squareup.picasso.Picasso

class NotificationAdapter (private var notificationsList: List<XX>, private val context: Context): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){
    class MyViewHolder(private var binding: NotificationspartdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: XX) {
            binding.notificationDescription.text = data.body
            binding.storename.text = data.text
            binding.dayandtime.text = data.delivery_date_time
            Picasso.get().load(data.image).into(binding.notificationimg)
            val context = itemView.context

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.MyViewHolder {
        val binding =
            NotificationspartdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationAdapter.MyViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
        val data = notificationsList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }
}