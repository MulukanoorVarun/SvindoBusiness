package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.databinding.ActivityCatalogueBinding
import`in`.webgrid.svindobusiness.fragements.*
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.google.android.material.tabs.TabLayout



@SuppressLint("StaticFieldLeak")
private lateinit var catalogueBinding: ActivityCatalogueBinding
@SuppressLint("Registered")
class catalogueActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar   // creating object of ToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedPreference = SharedPreference(this)
        catalogueBinding = ActivityCatalogueBinding.inflate(layoutInflater)
        setContentView(catalogueBinding.root)



        catalogueBinding.bottomnavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                    false
                }
                R.id.orders -> {
                    val intent = Intent(this, OrdersScreen::class.java)
                    overridePendingTransition(0,0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)

                    false
                }
                R.id.catalogue -> {
                    true
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
//
//        // To make our toolbar show the application
//        // we need to give it to the ActionBar
//        setSupportActionBar(bar)
//
//        // Initializing the ViewPagerAdapter
//        val adapter = ViewPagerAdapter(supportFragmentManager)
//
//        // add fragment to the list
//        adapter.addFragment(ProductsFragment(), "Products")
//        adapter.addFragment(CategoryFragment(), "Category")
//        adapter.addFragment(AddonFragment(), "Addon")
//        adapter.addFragment(SpotlightFragment(), "Spotlight")
//
//        // Adding the Adapter to the ViewPager
//        pager.adapter = adapter
//        // bind the viewPager with the TabLayout.
//        tab.setupWithViewPager(pager)
    }
}