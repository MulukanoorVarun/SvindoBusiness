package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.modelclass.AddOn
import com.example.vendorapp.modelclass.Maincategory
import com.squareup.picasso.Picasso

class AddonsListSpinnerAdapter (private val context: Context, private val itemList: List<AddOn>) : BaseAdapter() {
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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.addonslistspinnerlayout, parent, false)

        val item = itemList[position]
        val textView = view.findViewById<TextView>(R.id.spinnertext)
        val textView1 = view.findViewById<TextView>(R.id.spinnertext1)
        val textView2 = view.findViewById<TextView>(R.id.spinnertext2)

        textView.text = item.name
        textView1.text = item.description
        textView2.text = item.price
        // Glide.with(context).load(item.image).into(imageView)
       // Picasso.get().load(item.image).into(imageView)
        return view
    }
}
