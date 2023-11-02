package `in`.webgrid.svindobusiness.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.activity.ProductDetailsActivity
import `in`.webgrid.svindobusiness.modelclass.ProductXX


internal class GridViewAdapter(
    // on below line we are creating two
    // variables for course list and context
    private val productsList: List<ProductXX>,
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var productname: TextView
    private lateinit var productimage: ImageView
    private lateinit var ViewCard: View
    private var onItemClickListener: ((ProductXX) -> Unit)? = null
    // below method is use to return the count of course list
    override fun getCount(): Int {
        return productsList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): ProductXX? {

        return productsList[position]!!
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return (productsList[position].id).toLong()
    }

    // in below function we are getting individual item of grid view.
    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.gridview_item_layout, null)
        }

        productname = convertView!!.findViewById(R.id.productname)
        productimage = convertView!!.findViewById(R.id.productImage)
        ViewCard = convertView!!.findViewById(R.id.cardclicking)

        if(productsList.size>=0) {
            val url = productsList[position].image
        //    Picasso.with(context).load(url).resize(500, 500).into(productimage)
            Picasso.get().load(url).into(productimage)
            // on below line we are setting text in our course text view.
            productname.setText((getItem(position)?.name))

            ViewCard.setOnClickListener {
                context.startActivity(Intent(context, ProductDetailsActivity::class.java)
                    .putExtra("product_id", productsList[position].id))
               // Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show()
            }

        }
        // at last we are returning our convert view.
        return convertView
    }

    fun setOnItemClickListener(listener: (ProductXX) -> Unit) {
        onItemClickListener = listener
    }

}