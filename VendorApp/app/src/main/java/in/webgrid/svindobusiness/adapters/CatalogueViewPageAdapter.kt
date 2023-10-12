package `in`.webgrid.svindobusiness.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import`in`.webgrid.svindobusiness.fragements.AddonFragment
import`in`.webgrid.svindobusiness.fragements.CategoryFragment
import`in`.webgrid.svindobusiness.fragements.ProductsFragment
import`in`.webgrid.svindobusiness.fragements.SpotlightFragment

class CatalogueViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4 // Number of child fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductsFragment()
            1 -> CategoryFragment()
            2 -> AddonFragment()
            3 -> SpotlightFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}