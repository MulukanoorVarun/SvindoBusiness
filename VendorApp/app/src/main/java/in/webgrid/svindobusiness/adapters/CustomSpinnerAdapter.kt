package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.modelclass.Maincategory
import com.squareup.picasso.Picasso


class CustomSpinnerAdapter (private val context: Context, private val itemList: List<Maincategory>) : BaseAdapter() {
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
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.spinneritemdesign, parent, false)

        val item = itemList[position]
        val textView = view.findViewById<TextView>(R.id.spinnertext)
        val imageView = view.findViewById<ImageView>(R.id.productimage)

        textView.text = item.name
        // Glide.with(context).load(item.image).into(imageView)
        Picasso.get().load(item.image).into(imageView)
        return view
    }
}



