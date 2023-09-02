package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.modelclass.MaincategoryX
import com.example.vendorapp.modelclass.Subcategory

class SubCategoryAdapter(private val context: Context, private val itemList: List<Subcategory>) : BaseAdapter() {
    override fun getCount(): Int {

        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinneritemlayout, parent, false)
        val item = itemList[position]
        val textView = view.findViewById<TextView>(R.id.spinneritemtext)
        textView.text = item.name
//        val imgView = view.findViewById<ImageView>(android.R.id.icon)
//        Picasso.get().load(item.image).into(binding.storeImage)
//        Glide.with(context).load(item.image).into(imgView)

        return view
    }
}