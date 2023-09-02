package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.databinding.InsightsitemdesignBinding
import com.example.vendorapp.modelclass.Toprated
import com.squareup.picasso.Picasso

class TopratedAdapter (private var toprated: List<Toprated>, private val context: Context): RecyclerView.Adapter<TopratedAdapter.ViewHolder>() {
    class ViewHolder(private var binding: InsightsitemdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Toprated) {
            binding.itemName.text = data.name
            Picasso.get().load(data.image).into(binding.productImage)
            val context = itemView.context

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopratedAdapter.ViewHolder {
        val binding =
            InsightsitemdesignBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TopratedAdapter.ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: TopratedAdapter.ViewHolder, position: Int) {
        val data = toprated[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return toprated.size
    }
    }
