package `in`.webgrid.svindobusiness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.modelclass.CarouselItem
import `in`.webgrid.svindobusiness.modelclass.Image

class CarouselAdapter(private val items:List<Image>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Image) {
            // Bind data to the item view, e.g., set the image resource
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            Picasso.get().load(item.image).into(imageView)
        }
    }
}