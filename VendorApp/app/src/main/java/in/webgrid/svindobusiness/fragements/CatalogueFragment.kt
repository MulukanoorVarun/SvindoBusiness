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
class CatalogueFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var sharedPreference: SharedPreference
    private var catalogueBinding: FragmentCatalogueBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(requireContext())
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        val root = inflater?.inflate(R.layout.fragment_catalogue, container, false)
//        return inflater.inflate(R.layout.fragment_catalogue, container, false)
        catalogueBinding = FragmentCatalogueBinding.inflate(inflater, container, false)
        return catalogueBinding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  catalogueBinding = FragmentCatalogueBinding.bind(view)
        setupViewPager(view)
    }

    private fun setupViewPager(view: View) {
        val viewPager: ViewPager2 = catalogueBinding!!.viewpager
        val tabs: TabLayout = catalogueBinding!!.tabviews
        viewPager.adapter = CatalogueViewPageAdapter(childFragmentManager, lifecycle)
     //   catalogueBinding!!.viewpager.adapter = adapter

        TabLayoutMediator(catalogueBinding!!.tabviews,catalogueBinding!!.viewpager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab_layout) // Set custom layout for each tab
            val tabView = tab.customView as LinearLayout
            val tabTitle = tabView.findViewById<TextView>(R.id.tab_title)
            tabTitle.text = when (position) {
                0 -> "Products"
                1 -> "Category"
                2 -> "Addon"
                3 -> "Spotlight"
                else -> ""
            }
            val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.custom_tab_width) // Set the desired width in pixels or any other dimension
        tabView.layoutParams = layoutParams
    }.attach()
    }

}
