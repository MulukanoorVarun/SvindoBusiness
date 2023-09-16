package com.svindo.vendorapp.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.svindo.vendorapp.modelclass.Maincategory
import java.util.*
import kotlin.collections.ArrayList

class CustomProductsSoinnerAdapter(private val dataList: List<Maincategory>, private val context: AppCompatActivity) : BaseAdapter(), Filterable {

    private val inflater = context.layoutInflater
    private var filteredData:List<Maincategory> = dataList

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int):Any {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val itemText = view.findViewById<TextView>(android.R.id.text1)
        itemText.text = filteredData[position].name
        return view
    }
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return view
//    }


//    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = convertView
//            ?: LayoutInflater.from(context).inflate(R.layout., parent, false)
//        val textView = view.findViewById<TextView>(R.id.selecttext)
//        textView.text= filteredData[position].name
//        return view
//    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charString = constraint.toString()
                filteredData = if (charString.isEmpty()) { dataList }
                else {
                    val filteredList = ArrayList<Maincategory>()
                    for (item in dataList) {
                        if (item.toString().toLowerCase(Locale.getDefault()).contains(charString.toLowerCase(Locale.getDefault()))) {
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredData = results?.values as List<Maincategory>
                notifyDataSetChanged()
            }
        }
    }

}