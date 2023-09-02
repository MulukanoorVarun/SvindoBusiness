package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.databinding.AddColorLayoutBinding
import com.example.vendorapp.modelclass.ColourData


class ColorsaAdapter(private var colorsList: List<ColourData>,private val context: Context) : RecyclerView.Adapter<ColorsaAdapter.ViewHolder>() {
    class ViewHolder(private var binding: AddColorLayoutBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: ColourData) {
//            binding.ColorName.editableText = data.name
//            binding.ColorCode.editableText = data.code
            val context = itemView.context
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorsaAdapter.ViewHolder {
        val binding =
            AddColorLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorsaAdapter.ViewHolder(binding, context)
    }
    override fun onBindViewHolder(holder: ColorsaAdapter.ViewHolder, position: Int) {
        val data = colorsList[position]
        holder.bind(data)
    }
    override fun getItemCount(): Int {
        return colorsList.size
    }

    }