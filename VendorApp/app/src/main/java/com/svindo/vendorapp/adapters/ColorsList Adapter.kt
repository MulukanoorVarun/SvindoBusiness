package com.svindo.vendorapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.svindo.vendorapp.R

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var colorList: List<String> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(colorList: List<String>) {
        this.colorList = colorList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.colorslayoutdesign, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorName = colorList[position]
        holder.colorTextView.text = colorName
    }

    override fun getItemCount(): Int = colorList.size

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorTextView: TextView = itemView.findViewById(R.id.colorname)
    }
}


