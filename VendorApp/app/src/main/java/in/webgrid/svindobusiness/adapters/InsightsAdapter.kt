package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.databinding.InsightsitemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.Toplike
import com.squareup.picasso.Picasso

class InsightsAdapter (private var toplikelist: List<Toplike>, private val context: Context): RecyclerView.Adapter<InsightsAdapter.ViewHolder>() {
    class ViewHolder(private var binding: InsightsitemdesignBinding,private var context: Context?) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Toplike) {
            binding.itemName.text = data.name
            Picasso.get().load(data.image).into(binding.productImage)
             context = itemView.context
        }
    }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): InsightsAdapter.ViewHolder {
            val binding =
                InsightsitemdesignBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return InsightsAdapter.ViewHolder(binding, context)
        }

        override fun onBindViewHolder(holder: InsightsAdapter.ViewHolder, position: Int) {
            val data = toplikelist[position]
            holder.bind(data)
        }

        override fun getItemCount(): Int {
            return toplikelist.size
        }

    }
