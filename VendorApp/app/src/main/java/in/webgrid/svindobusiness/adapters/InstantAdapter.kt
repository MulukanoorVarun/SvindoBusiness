package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.activity.AcceptOrder
import`in`.webgrid.svindobusiness.databinding.OrderitemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.OrderX
import com.squareup.picasso.Picasso

class InstantAdapter (private var pendingOrdersList: List<OrderX>, private val context: Context): RecyclerView.Adapter<InstantAdapter.ViewHolder>() {
    class ViewHolder(private var binding: OrderitemdesignBinding, private var context: Context) : RecyclerView.ViewHolder(binding.root){


        fun bind(data: OrderX) {
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.orderid.text = data.order_number
            binding.itemno.text = data.tot_items
            binding.cost.text = data.amount
            binding.dayandtime.text = data.delivery_date
            binding.paymentType.text = data.payment_type
//            ("₹"+data.net_amount).also { binding.amount.text = it }
            Picasso.get().load(data.image_path).into(binding.storeImage)
            context = itemView.context


            binding.cardclicking.setOnClickListener {
                val intent = Intent(context, AcceptOrder::class.java)
                intent.putExtra("order_id",  data.id)
                context?.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OrderitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = pendingOrdersList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return pendingOrdersList.size
    }
}

