package `in`.webgrid.svindobusiness.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import`in`.webgrid.svindobusiness.databinding.BannerslayoutBinding
import`in`.webgrid.svindobusiness.modelclass.BannerXXX
import com.squareup.picasso.Picasso

class BannersAdapter (private var bannersList: List<BannerXXX>, private val context: Context): RecyclerView.Adapter<BannersAdapter.ViewHolder>()  {
    class ViewHolder(private var binding: BannerslayoutBinding, private var context: Context?) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BannerXXX) {

            Picasso.get().load(data.image).into(binding.bannerImage)
            val context = itemView.context

            binding.cardclicking.setOnClickListener {
                var url=data.url
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context?.startActivity(browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
               // startActivity(browserIntent)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannersAdapter.ViewHolder {
        val binding =
            BannerslayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannersAdapter.ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: BannersAdapter.ViewHolder, position: Int) {
        val data = bannersList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return bannersList.size
    }

}