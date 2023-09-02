package com.example.vendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.databinding.AcceptorderlayoutdesignBinding
import com.example.vendorapp.databinding.ViewaddonslayoutBinding
import com.example.vendorapp.modelclass.AddonX
import com.example.vendorapp.modelclass.OrderDetailsList
import com.squareup.picasso.Picasso

class ViewAddonsAdapter (private var addonsList: List<AddonX>, private val context: Context): RecyclerView.Adapter<ViewAddonsAdapter.ViewHolder>(){
    class ViewHolder(private var binding: ViewaddonslayoutBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){
        private lateinit var builder: AlertDialog.Builder
        private lateinit var alertDialog: AlertDialog

        fun bind(data: AddonX) {
            binding.addonname.text = data.name
           // binding.addonprice.text = data.price
            binding.proddductname.text = data.product_name


            context = itemView.context

//            binding.viewaddons.setOnClickListener {
//                showAlertDialog()
//            }
        }
//        fun showAlertDialog() {
//            builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
//          // val binding=ViewaddonslayoutBinding.inflate(layoutInflater)
//            builder.setView(binding.root)
//            builder.setCancelable(true)
//            alertDialog = builder.create()
//            alertDialog.show()
//            alertDialog.setCanceledOnTouchOutside(false)
//        }




    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAddonsAdapter.ViewHolder {
        val binding = ViewaddonslayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAddonsAdapter.ViewHolder(binding, context)
    }
    override fun getItemCount(): Int {
        return addonsList.size
    }
    override fun onBindViewHolder(holder: ViewAddonsAdapter.ViewHolder, position: Int) {
        val data = addonsList[position]
        holder.bind(data)
    }

}