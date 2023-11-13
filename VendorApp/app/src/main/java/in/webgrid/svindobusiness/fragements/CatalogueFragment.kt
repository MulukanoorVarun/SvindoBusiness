package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.CatalogueViewPageAdapter
import`in`.webgrid.svindobusiness.databinding.FragmentCatalogueBinding
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import `in`.webgrid.svindobusiness.adapters.ViewPagerAdapter

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
        viewPager.setOffscreenPageLimit(3)
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

    override fun onDestroyView() {
        super.onDestroyView()
        catalogueBinding = null
    }

}


//
//class CatalogueFragment : Fragment() {
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_catalogue, container, false)
//
//        val tab_toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
//        val tab_viewpager = view.findViewById<ViewPager>(R.id.viewPager)
//        val tab_tablayout = view.findViewById<TabLayout>(R.id.tabviews)
//
//        // Set the Toolbar as the ActionBar
//            (requireActivity() as AppCompatActivity).setSupportActionBar(tab_toolbar)
//
//        setupViewPager(tab_viewpager)
//        tab_tablayout.setupWithViewPager(tab_viewpager)
//
//        return view
//    }
//
//    private fun setupViewPager(viewpager: ViewPager) {
//        val adapter = ViewPagerAdapter(childFragmentManager)
//        adapter.addFragment(ProductsFragment(), "Products")
//        adapter.addFragment(CategoryFragment(), "Category")
//        adapter.addFragment(AddonFragment(), "Addon")
//        adapter.addFragment(SpotlightFragment(), "Spotlight")
//
//        adapter.also { viewpager.adapter = it }
//    }
//
//    inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
//        private val fragmentList1 = ArrayList<Fragment>()
//        private val fragmentTitleList1 = ArrayList<String>()
//
//        override fun getItem(position: Int): Fragment {
//            return fragmentList1[position]
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return fragmentTitleList1[position]
//        }
//
//        override fun getCount(): Int {
//            return fragmentList1.size
//        }
//
//        fun addFragment(fragment: Fragment, title: String) {
//            fragmentList1.add(fragment)
//            fragmentTitleList1.add(title)
//        }
//    }
//}
//

