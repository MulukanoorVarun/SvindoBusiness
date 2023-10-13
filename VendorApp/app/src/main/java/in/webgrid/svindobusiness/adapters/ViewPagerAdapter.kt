package `in`.webgrid.svindobusiness.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import`in`.webgrid.svindobusiness.fragements.GeneralDeliveryFragment
import`in`.webgrid.svindobusiness.fragements.InstantsFragment
import`in`.webgrid.svindobusiness.fragements.SelfPickupFragment
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

