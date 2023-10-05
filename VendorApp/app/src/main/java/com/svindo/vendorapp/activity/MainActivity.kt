package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.ActivityMainBinding
import com.svindo.vendorapp.fragements.AccountsFragment
import com.svindo.vendorapp.fragements.CatalogueFragment
import com.svindo.vendorapp.fragements.HomeFragment
import com.svindo.vendorapp.fragements.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeFragment())
        bottomNavigationView = findViewById(R.id.bottomnavigation) as BottomNavigationView
     bottomNavigationView.setOnItemSelectedListener {
       //  binding.bottomnavigation.setBackgroundResource(R.drawable.button_loading_background);
//         bottomNavigationView.isVisible=false
//         bottomNavigationView.isEnabled = false;
//         bottomNavigationView.
            when (it.itemId){
                R.id.home -> {
                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = false
                    loadFragment(HomeFragment())

                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = true
                    true
                }
                R.id.orders -> {
                    loadFragment(OrdersFragment())
                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = false

                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = true

                    true
                }

                R.id.catalogue -> {
                    loadFragment(CatalogueFragment())
                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = false

                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = true

                    true
                }

                R.id.account -> {
                    loadFragment(AccountsFragment())
                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = false

                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
                    true
                }
                else -> {
                  false
                }
            }
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Really Exit?")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(
                android.R.string.yes
            ) { arg0, arg1 ->
                setResult(RESULT_OK, Intent().putExtra("EXIT", true))
//                val i = Intent(this@BusinessdetailsActivity, SplashActivity::class.java)
//                startActivity(i)
                moveTaskToBack(true)
                exitProcess(-1)
            }.create().show()
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}