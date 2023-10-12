package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.activity.AddBannersActivity
import`in`.webgrid.svindobusiness.databinding.SpinneritemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.Product
import com.squareup.picasso.Picasso

class ProductsListAdapter(private var productsList: List<Product>, private val context: Context): RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {
    class ViewHolder(private var binding: SpinneritemdesignBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Product) {
            binding.spinnertext.text = data.name
            Picasso.get().load(data.image).into(binding.productimage)
            context = itemView.context


            binding.spinnertext.setOnClickListener{
                val intent = Intent(context, AddBannersActivity::class.java)
                intent.putExtra("product_id",  data.id)
                Toast.makeText(context,data.id, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsListAdapter.ViewHolder {
        val binding =
            SpinneritemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsListAdapter.ViewHolder(binding, context)
    }
    override fun getItemCount(): Int {
        return productsList.size
    }
    override fun onBindViewHolder(holder: ProductsListAdapter.ViewHolder, position: Int) {
        val data = productsList[position]
        holder.bind(data)
    }
}