package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.databinding.ActivityBankaccountBinding
import `in`.webgrid.svindobusiness.databinding.ActivityNetworkIssueBinding
import kotlin.system.exitProcess

@SuppressLint("Registered")
class NetworkIssueActivity : AppCompatActivity() {
    private lateinit var networkIssueBinding: ActivityNetworkIssueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_issue)
        networkIssueBinding = ActivityNetworkIssueBinding.inflate(layoutInflater)
        setContentView(networkIssueBinding.root)

        networkIssueBinding.retrybtn.setOnClickListener {
            if (checkForInternet(this)) {
                //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                val i = Intent(this@NetworkIssueActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            } else {
                Toast.makeText(this, "No Internet Connection,Please try again!", Toast.LENGTH_SHORT).show()
            }
        }

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
}