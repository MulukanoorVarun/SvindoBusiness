package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.ViewPagerAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentOrdersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


@SuppressLint("StaticFieldLeak")
private lateinit var ordersBinding: FragmentOrdersBinding

class OrdersFragment : Fragment(){
    private var ordersBinding: FragmentOrdersBinding? = null
    private val binding get() = ordersBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater?.inflate(R.layout.fragment_orders, container, false)

        return inflater.inflate(R.layout.fragment_orders, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ordersBinding = FragmentOrdersBinding.bind(view)
        setupViewPager(view)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun setupViewPager(view: View) {
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter


//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                // Prevent swiping by resetting the current item to its original position
//                viewPager.currentItem = 0 // Change this to the desired fixed page index
//            }
//        })
        viewPager.isUserInputEnabled = false


        TabLayoutMediator(binding.tabs,binding.viewPager ) { tab, position ->
            tab.setCustomView(R.layout.custom_tab_layout) // Set custom layout for each tab
            val tabView = tab.customView as LinearLayout
            val tabTitle = tabView.findViewById<TextView>(R.id.tab_title)
            tabTitle.text = when (position) {
                0 -> "Instant"
                1 -> "General Delivery"
                2 -> "SelfPickup"
                else -> ""
            }
        val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.custom_tab_width) // Set the desired width in pixels or any other dimension
        tabView.layoutParams = layoutParams

            tabView.setOnClickListener {
                // Handle tab click here
                // You can perform actions like changing the current item in the ViewPager2
                viewPager.currentItem = position
            }
    }.attach()
    }
    }