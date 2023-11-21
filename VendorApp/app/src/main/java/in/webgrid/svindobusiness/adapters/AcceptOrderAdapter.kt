package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.databinding.AcceptorderlayoutdesignBinding
import`in`.webgrid.svindobusiness.modelclass.OrderDetailsList
import com.squareup.picasso.Picasso

class AcceptOrderAdapter(private var ordersList: List<OrderDetailsList>, private val context: Context): RecyclerView.Adapter<AcceptOrderAdapter.ViewHolder>(){
    class ViewHolder(private var binding: AcceptorderlayoutdesignBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){
        private lateinit var builder: AlertDialog.Builder
        private lateinit var alertDialog: AlertDialog

        fun bind(data: OrderDetailsList) {
            binding.productname.text = data.name ?: " "
            binding.productunit.text = data.unit ?: " "
            //binding.productquanity.text = data.quantity
            binding.productcount.text = data.cart_quantity ?: " "
            binding.productprice.text = data.sale_price ?: " "
            binding.productprice2.text = data.total_price ?: " "
            Picasso.get().load(data.image?: " ").into(binding.productImage)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptOrderAdapter.ViewHolder {
        val binding = AcceptorderlayoutdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AcceptOrderAdapter.ViewHolder(binding, context)
    }
    override fun getItemCount(): Int {
        return ordersList.size
    }
    override fun onBindViewHolder(holder: AcceptOrderAdapter.ViewHolder, position: Int) {
        val data = ordersList[position]
        holder.bind(data)
    }

}