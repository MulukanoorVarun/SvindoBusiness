package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.databinding.InsightsitemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.Mostbuy
import com.squareup.picasso.Picasso

class MostByAdapter (private var mostby: List<Mostbuy>, private val context: Context): RecyclerView.Adapter<MostByAdapter.ViewHolder>() {
    class ViewHolder(private var binding: InsightsitemdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Mostbuy) {
            binding.itemName.text = data.name
            Picasso.get().load(data.image).into(binding.productImage)
             context = itemView.context
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MostByAdapter.ViewHolder {
        val binding =
            InsightsitemdesignBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MostByAdapter.ViewHolder(binding, context)
    }
    override fun onBindViewHolder(holder: MostByAdapter.ViewHolder, position: Int) {
        val data = mostby[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return mostby.size
    }
}
