package com.example.vendorapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.activity.AcceptOrder
import com.example.vendorapp.activity.AddBannersActivity
import com.example.vendorapp.databinding.SpinneritemdesignBinding
import com.example.vendorapp.modelclass.Maincategory
import com.example.vendorapp.modelclass.Product
import com.example.vendorapp.utils.showToast
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