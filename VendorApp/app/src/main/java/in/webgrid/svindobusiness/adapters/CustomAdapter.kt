package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import`in`.webgrid.svindobusiness.R

class CustomAdapter(context: Context, private val data: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_list_item_layout, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.custom_list_item_layout, parent, false)

        val itemNameTextView = rowView.findViewById<TextView>(R.id.item_name)
        val itemDescriptionTextView = rowView.findViewById<TextView>(R.id.item_description)
        val itemPriceTextView = rowView.findViewById<TextView>(R.id.item_price)

        val dataIndex = position * 3
        if (dataIndex + 2 < data.size){
            val name = data[dataIndex]
            val description = data[dataIndex + 1]
            val price = data[dataIndex + 2]

            // Set data to views
            itemNameTextView.text = name
            itemDescriptionTextView.text = description
            itemPriceTextView.text = price
        }
        return rowView
    }
}
