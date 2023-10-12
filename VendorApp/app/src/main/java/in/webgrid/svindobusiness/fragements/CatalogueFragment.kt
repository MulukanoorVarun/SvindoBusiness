package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.CatalogueViewPageAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentCatalogueBinding
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@SuppressLint("StaticFieldLeak")
private lateinit var catalogueBinding: FragmentCatalogueBinding
//
//class CatalogueFragment : Fragment() {
//
//    private lateinit var sharedPreference: SharedPreference
//    private var catalogueBinding: FragmentCatalogueBinding? = null
//    private val Binding get() = catalogueBinding!!
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreference= SharedPreference(requireContext())
//        }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        val root = inflater?.inflate(R.layout.fragment_catalogue, container, false)
//        return inflater.inflate(R.layout.fragment_catalogue, container, false)
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        catalogueBinding = FragmentCatalogueBinding.bind(view)
//        setupViewPager(view)
//    }
//
//    private fun setupViewPager(view: View) {
//        val viewPager: ViewPager2 = view.findViewById(R.id.viewpager)
//        val tabs: TabLayout = view.findViewById(R.id.tabviews)
//        val adapter = CatalogueViewPageAdapter(childFragmentManager, lifecycle)
//        Binding.viewpager.adapter = adapter
//
//        TabLayoutMediator(Binding.tabviews,Binding.viewpager ) { tab, position ->
//            tab.setCustomView(R.layout.custom_tab_layout) // Set custom layout for each tab
//            val tabView = tab.customView as LinearLayout
//            val tabTitle = tabView.findViewById<TextView>(R.id.tab_title)
//            tabTitle.text = when (position) {
//                0 -> "Products"
//                1 -> "Category"
//                2 -> "Addon"
//                3 -> "Spotlight"
//                else -> ""
//            }
//            val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.width = resources.getDimensionPixelSize(R.dimen.custom_tab_width) // Set the desired width in pixels or any other dimension
//        tabView.layoutParams = layoutParams
//    }.attach()
//
//
//    }
//
//
//}


class CatalogueFragment : Fragment() {
    private var catalogueBinding: FragmentCatalogueBinding? = null
    private val Binding get() = catalogueBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        catalogueBinding = FragmentCatalogueBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(view)
    }

    private fun setupViewPager(view: View) {
        val viewPager: ViewPager2 = Binding.viewpager
        val tabs: TabLayout = Binding.tabviews

        val adapter = CatalogueViewPageAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val tabTitle = when (position) {
                0 -> "Products"
                1 -> "Category"
                2 -> "Addon"
                3 -> "Spotlight"
                else -> ""
            }

            if (tabTitle.isNotEmpty()) {
                tab.setCustomView(R.layout.custom_tab_layout)
                val tabView = tab.customView as LinearLayout
                val tabTitleView = tabView.findViewById<TextView>(R.id.tab_title)
                tabTitleView.text = tabTitle
                val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.custom_tab_width)
                tabView.layoutParams = layoutParams
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        catalogueBinding = null
    }
}