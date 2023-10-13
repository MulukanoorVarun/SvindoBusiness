package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.databinding.ActivityMainBinding
import`in`.webgrid.svindobusiness.fragements.AccountsFragment
import`in`.webgrid.svindobusiness.fragements.CatalogueFragment
import`in`.webgrid.svindobusiness.fragements.HomeFragment
import`in`.webgrid.svindobusiness.fragements.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
//private lateinit var binding: ActivityMainBinding
//class MainActivity : AppCompatActivity() {
//    lateinit var bottomNavigationView: BottomNavigationView
//    @SuppressLint("SuspiciousIndentation")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//       binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        loadFragment(HomeFragment())
//        bottomNavigationView = findViewById(R.id.bottomnavigation) as BottomNavigationView
//     bottomNavigationView.setOnItemSelectedListener {
//
//         bottomNavigationView.menu.findItem(R.id.orders).isEnabled = false
//         bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = false
//         bottomNavigationView.menu.findItem(R.id.account).isEnabled = false
//         bottomNavigationView.menu.findItem(R.id.account).isEnabled = false
//
//            when (it.itemId){
//                R.id.home -> {
//                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = false
//                    loadFragment(HomeFragment())
//                    Handler().postDelayed({
//                        bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
//                        bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
//                        bottomNavigationView.menu.findItem(R.id.account).isEnabled = true
//                    }, 1000)
//                    true
//                }
//                R.id.orders -> {
//                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = false
//                    loadFragment(OrdersFragment())
//                    Handler().postDelayed({
//                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = true
//                    }, 1000)
//
//                    true
//                }
//
//                R.id.catalogue -> {
//                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = false
//                    loadFragment(CatalogueFragment())
//                    Handler().postDelayed({
//                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = true
//                    }, 1000)
//
//                    true
//                }
//
//                R.id.account -> {
//                    bottomNavigationView.menu.findItem(R.id.account).isEnabled = false
//                    loadFragment(AccountsFragment())
//                    Handler().postDelayed({
//                    bottomNavigationView.menu.findItem(R.id.home).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.orders).isEnabled = true
//                    bottomNavigationView.menu.findItem(R.id.catalogue).isEnabled = true
//                    }, 1000)
//                    true
//                }
//                else -> {
//                  false
//                }
//            }
//            true
//        }
//    }
//
//    @SuppressLint("MissingSuperCall")
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        AlertDialog.Builder(this)
//            .setTitle("Really Exit?")
//            .setMessage("Are you sure you want to exit?")
//            .setNegativeButton(android.R.string.no, null)
//            .setPositiveButton(
//                android.R.string.yes
//            ) { arg0, arg1 ->
//                setResult(RESULT_OK, Intent().putExtra("EXIT", true))
////                val i = Intent(this@BusinessdetailsActivity, SplashActivity::class.java)
////                startActivity(i)
//                moveTaskToBack(true)
//                exitProcess(-1)
//            }.create().show()
//    }
//    private fun loadFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container, fragment)
//        transaction.commit()
//    }
//}


class MainActivity : AppCompatActivity() {

    private val fragmentManager: FragmentManager = supportFragmentManager
    private var activeFragment: Fragment = HomeFragment()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.orders -> {
                replaceFragment(OrdersFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.catalogue -> {
                replaceFragment(CatalogueFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                replaceFragment(AccountsFragment())
                return@OnNavigationItemSelectedListener true
            }
            // Add more cases for other menu items as needed
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkForInternet(this)) {
            // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
           // Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NetworkIssueActivity::class.java)
            startActivity(intent)
        }

        val navigation = findViewById<BottomNavigationView>(R.id.bottomnavigation)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        replaceFragment(HomeFragment())
    }
    @SuppressLint("MissingSuperCall")
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

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // Add to back stack if you want to allow back navigation
        transaction.commit()

        activeFragment = fragment
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}