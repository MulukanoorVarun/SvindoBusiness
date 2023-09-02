package com.example.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.R
import com.example.vendorapp.adapters.ViewPagerAdapter
import com.example.vendorapp.databinding.ActivityOrdersScreenBinding
import com.example.vendorapp.fragements.GeneralDeliveryFragment
import com.example.vendorapp.fragements.InstantsFragment
import com.example.vendorapp.fragements.SelfPickupFragment
import com.example.vendorapp.utils.SharedPreference
import com.google.android.material.tabs.TabLayout

@SuppressLint("Registered")
private lateinit var ordersBinding: ActivityOrdersScreenBinding
class OrdersScreen : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar   // creating object of ToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // sharedPreference = SharedPreference(this)
        ordersBinding = ActivityOrdersScreenBinding.inflate(layoutInflater)
        setContentView(ordersBinding.root)


        ordersBinding.bottomnavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this,DashboardActivity::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                    false
                }
                R.id.orders -> {
                    true
                }
                R.id.catalogue -> {
                    val intent = Intent(this, catalogueActivity::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                    false
                }
                R.id.account -> {
                    val intent = Intent(this,ProfileSrceenActivity::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                    false
                }

                else -> false
            }
        }


//        pager = findViewById(R.id.viewPager)
//        tab = findViewById(R.id.tabs)
//        bar = findViewById(R.id.toolbar)
//        // To make our toolbar show the application
//        // we need to give it to the ActionBar
//        setSupportActionBar(bar)
//        // Initializing the ViewPagerAdapter
//        val adapter = ViewPagerAdapter(supportFragmentManager)
//        // add fragment to the list
//        adapter.addFragment(InstantsFragment(), "Instant")
//        adapter.addFragment(GeneralDeliveryFragment(), "General Delivery")
//        adapter.addFragment(SelfPickupFragment(), "Self Pickup")
//
//
//        // Adding the Adapter to the ViewPager
//        pager.adapter = adapter
//        // bind the viewPager with the TabLayout.
//        tab.setupWithViewPager(pager)
    }
}