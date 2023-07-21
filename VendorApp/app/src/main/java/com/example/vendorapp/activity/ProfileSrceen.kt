package com.example.vendorapp.activity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendorapp.R
import com.example.vendorapp.databinding.ActivityDashboardBinding
import com.example.vendorapp.databinding.ActivityOrdersScreenBinding
import com.example.vendorapp.databinding.ActivityProfileScreenBinding
import com.example.vendorapp.utils.SharedPreference


class ProfileSrceenActivity : AppCompatActivity() {
    private lateinit var accountBinding: ActivityProfileScreenBinding
    private lateinit var sharedPreference: SharedPreference
    //lateinit var dashboardResponse: Dashboard_Response
    private lateinit var linearLayoutManager: LinearLayoutManager
    // private lateinit var adapter: DashboardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountBinding = ActivityProfileScreenBinding.inflate(layoutInflater)
        sharedPreference = SharedPreference(this)
        setContentView(accountBinding.root)
        //get_update_status()
        accountBinding.bottomnavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
//                    val intent = Intent(this,DashboardActivity::class.java)
//                    overridePendingTransition(0,0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(intent)

                    false
                }
                R.id.orders -> {
                    val intent = Intent(this, OrdersScreen::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)

                    false
                }
                R.id.catalouge -> {
                    val intent = Intent(this, catalogueActivity::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)

                    false
                }
                R.id.account -> {
                   true
                }

                else -> false
            }
        }
    }
}
