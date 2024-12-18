package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.databinding.CouponsitemdesignBinding
import`in`.webgrid.svindobusiness.modelclass.X

class CouponListAdapter(private var couponsList: List<X>, private val context: Context): RecyclerView.Adapter<CouponListAdapter.ViewHolder>()  {
    class ViewHolder(private var binding: CouponsitemdesignBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: X) {
            //"#${data.store_name}".also { binding.storeName.text = it }
            binding.mycoupons.text = data.name
            binding.percentage.text = data.discount_percentage
            binding.validtime.text = data.expire_date
            binding.coupondesc.text = data.description
//            ("₹"+data.net_amount).also { binding.amount.text = it }
            //Picasso.get().load(data.image_path).into(binding.storeImage)
            val context = itemView.context
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CouponListAdapter.ViewHolder {
        val binding =
            CouponsitemdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponListAdapter.ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: CouponListAdapter.ViewHolder, position: Int) {
        val data = couponsList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return couponsList.size
    }
}