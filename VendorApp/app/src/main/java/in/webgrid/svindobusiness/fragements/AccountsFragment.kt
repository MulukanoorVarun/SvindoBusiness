package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.activity.*
import`in`.webgrid.svindobusiness.databinding.FragmentAccountsBinding
import`in`.webgrid.svindobusiness.databinding.ReportspopuplayoutBinding
import`in`.webgrid.svindobusiness.databinding.ShopboostlayoutBinding
import`in`.webgrid.svindobusiness.modelclass.AccountsModal
import`in`.webgrid.svindobusiness.modelclass.CashbackStatusModal
import`in`.webgrid.svindobusiness.modelclass.Verify_otp_Response
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
private lateinit var accountbinding: FragmentAccountsBinding
class  AccountsFragment : Fragment() {
    private lateinit var binding: ShopboostlayoutBinding
    private lateinit var reportbinding:ReportspopuplayoutBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var accountsresponse: AccountsModal
    lateinit var cashbackresponse: CashbackStatusModal
    lateinit var shopboostresponse: Verify_otp_Response
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    lateinit var progress: ProgressDialog
     var is_location=""
    var shop_status=""
    var shop_boost=0
    var cashbackstatus=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        accountbinding = FragmentAccountsBinding.inflate(layoutInflater)
        reportbinding=ReportspopuplayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountbinding = FragmentAccountsBinding.inflate(inflater, container, false)
        accountbinding = FragmentAccountsBinding.inflate(layoutInflater)
        binding = ShopboostlayoutBinding.inflate(layoutInflater)

        progress = ProgressDialog(context,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)

//          requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                // Permission granted, start the download
//                startdownload()
//            }
//        }
//        accountbinding.qrdownload.setOnClickListener {
//            requestStoragePermission()
//        }


        accountbinding.notification.setOnClickListener {
            val intent = Intent(getActivity(), NotificationsActivity::class.java)
            getActivity()?.startActivity(intent)
        }

//        accountbinding.storePic.setOnClickListener {
//            val intent = Intent(getActivity(), GoogleMapsActivity::class.java)
//            getActivity()?.startActivity(intent)
//        }

        accountbinding.insights.setOnClickListener {
            val intent = Intent(getActivity(), InsightsActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        accountbinding.discountcoupon.setOnClickListener {
            val intent = Intent(getActivity(), CouponsActivity::class.java)
            getActivity()?.startActivity(intent)
        }
//        accountbinding.addproduct.setOnClickListener {
//            val intent = Intent(getActivity(), AddPrintingProduct::class.java)
//            getActivity()?.startActivity(intent)
//        }

        accountbinding.logout.setOnClickListener {
            Logout()
            sharedPreference.clearSharedPreference()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            getActivity()?.startActivity(intent)
        }

        accountbinding.addBanners.setOnClickListener {
            val intent = Intent(getActivity(), AddBannersActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        accountbinding.payoutamount.setOnClickListener {
            val intent = Intent(getActivity(), PayOutAmountActivity::class.java)
            getActivity()?.startActivity(intent)
        }
        accountbinding.editbutton.setOnClickListener {
            val intent = Intent(getActivity(), EditBusinessdetails::class.java)
            getActivity()?.startActivity(intent)
        }


        accountbinding.customerFeedback.setOnClickListener {
            val intent = Intent(getActivity(), CustomerFeedback::class.java)
            getActivity()?.startActivity(intent)
        }


        accountbinding.addwallet.setOnClickListener {
            val intent = Intent(getActivity(), AdWalletActivity::class.java)
            getActivity()?.startActivity(intent)
        }


        accountbinding.privacyPolicy.setOnClickListener {
            var url="https://webgrid.in/projects/svindo/web/Webcontroller/privacypolicy"
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://$url"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }

        accountbinding.support.setOnClickListener {
            var supporturl="https://wa.me/918377935333"
            if (!supporturl.startsWith("http://") && !supporturl.startsWith("https://")) {
                supporturl = "http://$supporturl"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(supporturl))
            startActivity(browserIntent)
        }


        accountbinding.termsConditions.setOnClickListener {
            var termsurl="https://webgrid.in/projects/svindo/web/Webcontroller/terms"
            if (!termsurl.startsWith("http://") && !termsurl.startsWith("https://")) {
                termsurl = "http://$termsurl"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsurl))
            startActivity(browserIntent)
        }


        Accountdetails()
        accountbinding.locationsswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                is_location= "Open";
                LocationStatus(
                    is_location=is_location.toString().trim()
                )
            } else {
                is_location = "Closed";
                LocationStatus(
                    is_location=is_location.toString().trim()
                )
            }
        }

        accountbinding.cashbackswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true) {
                cashbackstatus= "enable"
                CashbackStatus(
                    cashbackstatus=cashbackstatus.toString().trim()
                )
            } else {
                cashbackstatus = "disable"
                CashbackStatus(
                    cashbackstatus=cashbackstatus.toString().trim()
                )
            }
        }
        accountbinding.shopstatusswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked ==true) {
                shop_status= "Permanently Closed";
                ShopStatus(
                    shop_status=shop_status.toString().trim()
                )
            }
            if(isChecked == false){
                shop_status = "Open";
                ShopStatus(
                    shop_status=shop_status.toString().trim()
                )
            }
        }
//        accountbinding.shopboostsswitch.setOnClickListener {
//            binding.submitbutton.setOnClickListener {
//            showAlertDialog()
//        }
//
//        accountbinding.shopboostsswitch.setOnClickListener {
//            if(accountbinding.shopboostsswitch.isChecked)
//            {
//                accountbinding.shopboostsswitch.isChecked=false
////                showAlertDialog(shop_boost.toString())
//            }
//        }
        accountbinding.shopboostsswitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked == true){
                shop_boost=1
                showAlertDialog(shop_boost.toString())
            }
            if(isChecked == false){
                shop_boost=0
                Shopboostunckeckeddeatils(
                    shop_boost= shop_boost.toString().trim(),
                )
            }
        }

        if (checkForInternet(requireContext())) {
           // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
          //  Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }


        return accountbinding.root
    }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

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
    internal fun showAlertDialog(shop_boost: String){
        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val rootView = binding.root
        // Check if the rootView already has a parent
        val parent = rootView.parent as? ViewGroup
        parent?.removeView(rootView)
        builder.setView(binding.root)
        builder.setCancelable(true)
        builder.setOnCancelListener(DialogInterface.OnCancelListener {
            accountbinding.shopboostsswitch.isChecked=false
        })
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
        binding.submitbutton.setOnClickListener {
            val max_amt=binding.maxamtet.text.toString().trim()
            if(max_amt.isNotEmpty()) {
                Shopboostdeatils(
                    binding.maxamtet.text.toString().trim(),
                    shop_boost.toString().trim()
                )
            }
           // alertDialog.hide()
        }
        }

    fun showreportsdialog(){
        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val rootView = reportbinding.root
        // Check if the rootView already has a parent
        val parent = rootView.parent as? ViewGroup
        parent?.removeView(rootView)
        builder.setView(reportbinding.root)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        reportbinding.submitbutton.setOnClickListener{
            val startDate=reportbinding.startdateEt.text.toString().trim()
            val EndDate=reportbinding.enddateEt.text.toString().trim()
            var reportsurl= accountsresponse.report
            reportsurl=reportsurl+"?"+"startdate="+startDate+"&"+"enddate="+EndDate
          //  Toast.makeText(context,reportsurl, Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reportsurl.trim()))
            startActivity(intent)
            alertDialog.hide()
        }
    }

    fun Shopboostdeatils(
        max_amt:String,
        shop_boost:String){
        progress.show()
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.ShopboostDetails(sharedPreference.getValueString("token"),shop_boost,max_amt)
            requestCall.enqueue(object : Callback<Verify_otp_Response>{
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                shopboostresponse = response.body()!!
                                if (shopboostresponse != null) {
                                    if (shopboostresponse.error == "0") {
                                      //  Accountdetails()
                                        alertDialog.hide()
                                        progress.dismiss()
//                                        accountbinding.shopboostsswitch.isChecked=true;
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                    if (shopboostresponse.error == "1") {
                                        alertDialog.hide()
                                        progress.dismiss()
                                        accountbinding.shopboostsswitch.isChecked=false
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                    if (shopboostresponse.error == "2") {
                                        alertDialog.hide()
                                        progress.dismiss()
                                        accountbinding.shopboostsswitch.isChecked=false
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }else{

                                    }
                                }else{

                                }
                            }
                                response.code() == 401 -> {
                                    Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: TimeoutException) {
                            Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                        }
                        override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                            //  dashboardBinding.progressBarLay.visibility  = View.GONE
                            Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
            } catch (e: Exception) {
                //dashboardBinding.progressBarLay.visibility = View.GONE
                Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    fun  Shopboostunckeckeddeatils(
        shop_boost:String){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.ShopboostDetails(sharedPreference.getValueString("token"),shop_boost,"0")
            requestCall.enqueue(object : Callback<Verify_otp_Response>{
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                shopboostresponse = response.body()!!
                                if (shopboostresponse != null) {
                                    if (shopboostresponse.error == "0") {
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                    if (shopboostresponse.error == "1") {
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                    if (shopboostresponse.error == "2") {
                                        Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                    }else{

                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun Accountdetails(){
        progress.show()
        accountbinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.AccountDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AccountsModal>{
                override fun onResponse(
                    call: Call<AccountsModal>,
                    response: Response<AccountsModal>
                ) {
                    accountbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    progress.dismiss()
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.body() != null) {
                                    accountsresponse = response.body()!!
                                    if (accountsresponse.error == "0") {
                                        accountbinding.storname.text =
                                            accountsresponse.details.store_name
                                        accountbinding.storemail.text =
                                            accountsresponse.details.email_id
                                        Picasso.get().load(accountsresponse.details.image)
                                            .into(accountbinding.storePic)
                                        accountbinding.availableamt.text =
                                            accountsresponse.details.available_amount
                                        accountbinding.cashbackpercent.text =
                                            accountsresponse.details.cashback_percentage

                                        binding.viewamt.text = accountsresponse.shop_view_cost
                                        binding.perclickcostamt.text =
                                            accountsresponse.shop_click_cost


                                        var boost_id = accountsresponse.details.is_boost
                                        if (boost_id == "0") {
                                            val unchecked = false
                                            accountbinding.shopboostsswitch.isChecked = unchecked
                                        } else {
                                            val checked = true
                                            accountbinding.shopboostsswitch.isChecked = checked
                                            alertDialog.hide()
                                        }


                                        accountbinding.qrdownload.setOnClickListener {
                                            var qrurl = accountsresponse.qr_link

                                            val intent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(qrurl.trim()))
                                            startActivity(intent)
                                        }

                                        accountbinding.reports.setOnClickListener {
                                            showreportsdialog()
                                        }


                                        var deliveryStatus = accountsresponse.details.free_delivery.toString()
                                        if (deliveryStatus == "enable") {
                                            accountbinding.cashbackswitch.isChecked = true
                                        } else{
                                            accountbinding.cashbackswitch.isChecked = false
                                        }

                                        var locationStatus =
                                            accountsresponse.details.is_location_visible
                                        if (locationStatus == "Open") {
                                            val enable = true
                                            accountbinding.locationsswitch.isChecked = enable
                                        } else if (locationStatus == "Closed") {
                                            val disable = false
                                            accountbinding.locationsswitch.isChecked = disable
                                        } else {

                                        }


                                        var shopStatus = accountsresponse.details.open_status
                                        if (shopStatus == "Permanently Closed") {
                                            val enable = true
                                            accountbinding.shopstatusswitch.isChecked = enable
                                        } else if (shopStatus == "Open") {
                                            val disable = false
                                            accountbinding.shopstatusswitch.isChecked = disable
                                        } else {

                                        }


                                    } else {

                                    }
                                } else {

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(
                                    context,
                                    getString(R.string.session_exp),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    context,
                                    getString(R.string.server_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context, getString(R.string.time_out), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                    override fun onFailure(call: Call<AccountsModal>, t: Throwable) {
                        progress.dismiss()
                        accountbinding.progressBarLay.progressBarLayout.visibility = View.GONE
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    } catch (e: Exception) {
        //dashboardBinding.progressBarLay.visibility = View.GONE
        Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
    }
}

//fun startdownload() {
//    val request =
//        DownloadManager.Request(Uri.parse(accountsresponse.details.qr_link!!))
//            .setTitle("File Download")
//            .setDescription("Downloading")
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            .setDestinationInExternalPublicDir(
//                Environment.DIRECTORY_DOWNLOADS,
//                "filename.extension"
//            )
//    val downloadManager =
//        requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    downloadManager.enqueue(request)
//}
//    private fun requestStoragePermission() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        requestPermissionLauncher.launch(intent)
//    }
//    companion object {
//        private const val PERMISSION_REQUEST_CODE = 1001
//    }

    fun Logout(){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.session_logout(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<Verify_otp_Response>{
                override fun onResponse(
                    call: Call<Verify_otp_Response>,
                    response: Response<Verify_otp_Response>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        shopboostresponse = response.body()!!
                                        if (shopboostresponse.error == "0") {
                                            Toast.makeText(context,shopboostresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                        } else{

                                        }
                                    } else {

                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<Verify_otp_Response>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    fun CashbackStatus(cashbackstatus:String){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.CashBackStatus(sharedPreference.getValueString("token"),cashbackstatus)
            requestCall.enqueue(object : Callback<CashbackStatusModal>{
                override fun onResponse(
                    call: Call<CashbackStatusModal>,
                    response: Response<CashbackStatusModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        cashbackresponse = response.body()!!
                                        if (cashbackresponse.error == "0") {
                                            Toast.makeText(context,cashbackresponse.message.toString(), Toast.LENGTH_SHORT).show()
//                                            var Status=cashbackresponse.status
//                                            if(Status=="enable"){
//                                                val enable=true
//                                                accountbinding.cashbackswitch.isChecked=enable
//                                            } else if(Status=="disable"){
//                                                val disable=false
//                                                accountbinding.cashbackswitch.isChecked=disable
//                                            }else{

                                            //}
                                        } else{

                                        }
                                    } else {

                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<CashbackStatusModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    fun LocationStatus(
        is_location:String,
    ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.LocationStatusDetails(sharedPreference.getValueString("token"),"location_status",is_location)
            requestCall.enqueue(object : Callback<CashbackStatusModal>{
                override fun onResponse(
                    call: Call<CashbackStatusModal>,
                    response: Response<CashbackStatusModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        cashbackresponse = response.body()!!
                                        if(cashbackresponse.error=="0"){
                                            Toast.makeText(context,cashbackresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                        }else{

                                        }
                                    } else {

                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<CashbackStatusModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }



    fun ShopStatus(
        shop_status:String,
    ){
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall =
                ordersService.ShopStatusDetails(sharedPreference.getValueString("token"),"permanently_closed",shop_status)
            requestCall.enqueue(object : Callback<CashbackStatusModal>{
                override fun onResponse(
                    call: Call<CashbackStatusModal>,
                    response: Response<CashbackStatusModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        cashbackresponse = response.body()!!
                                        if(cashbackresponse.error=="0") {
                                            Toast.makeText(context,cashbackresponse.message.toString(), Toast.LENGTH_SHORT).show()
                                        }else{

                                        }
                                    } else {

                                    }
                                }else{

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out), Toast.LENGTH_SHORT).show()
                    }
                override fun onFailure(call: Call<CashbackStatusModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    }
