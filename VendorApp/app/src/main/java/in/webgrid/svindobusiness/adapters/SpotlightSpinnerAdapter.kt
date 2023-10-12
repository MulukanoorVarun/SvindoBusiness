package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.modelclass.Product
import com.squareup.picasso.Picasso

class SpotlightSpinnerAdapter (private val context: Context, private val itemList: List<Product>) : BaseAdapter() {
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
        val textView1 = view.findViewById<TextView>(R.id.pricetxt)
        val imageView = view.findViewById<ImageView>(R.id.productimage)
//        val textView1 = view.findViewById<TextView>(R.id.productquanity)
//        val textView2 = view.findViewById<TextView>(R.id.productunit)
//        val textView3 = view.findViewById<TextView>(R.id.productcost)
//        val textView4 = view.findViewById<TextView>(R.id.productmrp)
//        val textView5 = view.findViewById<TextView>(R.id.productavalability)
        textView.text = item.name
        textView1.text = item.sale_price
//        textView1.text = item.quantity
//        textView2.text = item.unit
//        textView3.text = item.sale_price
//        textView4.text = item.mrp_price
//        textView5.text = item.in_stock

        // Glide.with(context).load(item.image).into(imageView)
        Picasso.get().load(item.image).into(imageView)
        return view
    }
}