package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.modelclass.AddonXX
import com.example.vendorapp.modelclass.MaincategoryX
import com.squareup.picasso.Picasso

class AddonIconsAdapter (private val context: Context, private val itemList: List<AddonXX>) : BaseAdapter() {
    override fun getCount(): Int {

        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.addonsspinneritemlayout, parent, false)

        val item = itemList[position]
      //  val textView = view.findViewById<TextView>(R.id.spinneritemtext)
        val imageView = view.findViewById<ImageView>(R.id.productimage)
        Picasso.get().load(item.image).into(imageView)
       // Glide.with(context).load(item.image).into(imageView)
        return view
    }


//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
//        val item = itemList[position]
//        val textView = view.findViewById<TextView>(android.R.id.text1)
//        textView.text = item.name
//        val imgView = view.findViewById<ImageView>(android.R.id.icon)
//        Glide.with(context).load(item.image).into(imgView)
//        return view
//    }
}