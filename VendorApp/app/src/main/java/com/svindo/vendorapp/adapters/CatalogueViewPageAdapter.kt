package com.svindo.vendorapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.svindo.vendorapp.fragements.AddonFragment
import com.svindo.vendorapp.fragements.CategoryFragment
import com.svindo.vendorapp.fragements.ProductsFragment
import com.svindo.vendorapp.fragements.SpotlightFragment

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