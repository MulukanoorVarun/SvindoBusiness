package com.example.vendorapp.fragements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.setContentView
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class OrdersFragment : Fragment() {
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar   // creating object of ToolBa

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

}




