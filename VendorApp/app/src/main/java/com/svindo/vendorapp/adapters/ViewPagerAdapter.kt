package com.svindo.vendorapp.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.svindo.vendorapp.fragements.GeneralDeliveryFragment
import com.svindo.vendorapp.fragements.InstantsFragment
import com.svindo.vendorapp.fragements.SelfPickupFragment
//
//class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
//    FragmentStatePagerAdapter(supportFragmentManager) {


    class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){

        override fun getItemCount(): Int {
            return 3 // Number of child fragments
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> InstantsFragment()
                1 -> GeneralDeliveryFragment()
                2 -> SelfPickupFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

    // declare arrayList to contain fragments and its title
//    private val mFragmentList = ArrayList<Fragment>()
//    private val mFragmentTitleList = ArrayList<String>()
//
//    override fun getItem(position: Int): Fragment {
//        // return a particular fragment page
//        return mFragmentList[position]
//    }
//
//    override fun getCount(): Int {
//        // return the number of tabs
//        return mFragmentList.size
//    }
//
//    override fun getPageTitle(position: Int): CharSequence{
//        // return title of the tab
//        return mFragmentTitleList[position]
//    }
//
//    fun addFragment(fragment: Fragment, title: String) {
//        // add each fragment and its title to the array list
//        mFragmentList.add(fragment)
//        mFragmentTitleList.add(title)
//    }
