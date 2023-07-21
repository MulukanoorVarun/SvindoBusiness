package com.example.vendorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.vendorapp.R
import com.example.vendorapp.databinding.ActivityDashboardBinding
import com.example.vendorapp.databinding.ActivityMainBinding
import com.example.vendorapp.fragements.AccountsFragment
import com.example.vendorapp.fragements.CatalogueFragment
import com.example.vendorapp.fragements.HomeFragment
import com.example.vendorapp.fragements.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main2)

        loadFragment(HomeFragment())

        bottomNavigationView = findViewById(R.id.bottomnavigation) as BottomNavigationView
     bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.orders -> {
                    loadFragment(OrdersFragment())
                    true
                }

                R.id.catalogue -> {
                    loadFragment(CatalogueFragment())
                    true
                }

                R.id.account -> {
                    loadFragment(AccountsFragment())
                    true
                }

                else -> {
                  false
                }

            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}