package `in`.webgrid.svindobusiness.fragements

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import`in`.webgrid.svindobusiness.R
import`in`.webgrid.svindobusiness.adapters.AddonIconsAdapter
import`in`.webgrid.svindobusiness.adapters.AddonsListAdapter
import`in`.webgrid.svindobusiness.databinding.*
import`in`.webgrid.svindobusiness.modelclass.*
import`in`.webgrid.svindobusiness.services.ApiClient
import`in`.webgrid.svindobusiness.services.ApiInterface
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import com.squareup.picasso.Picasso
import `in`.webgrid.svindobusiness.activity.NetworkIssueActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeoutException


@SuppressLint("StaticFieldLeak")
class AddonFragment : Fragment() {

    private lateinit var AddonBinding: FragmentAddonBinding
    private lateinit var binding: AddonlayoutdesignBinding

    private lateinit var sharedPreference: SharedPreference
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var addonsresponse: Bankdetails_Response
    private lateinit var addonsListresponse: AddonsListModal
    private lateinit var addonsIconsModalresponse: AddonIconsModal
    private lateinit var addon_details_response: Addon_details_modal
    private lateinit var adapter: AddonsListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private  var file_1: File? = null
    lateinit var progress: ProgressDialog

    private lateinit var spinner: Spinner
    var addon_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference= SharedPreference(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreference= SharedPreference(requireContext())

        AddonBinding = FragmentAddonBinding.inflate(inflater, container, false)
        AddonBinding = FragmentAddonBinding.inflate(layoutInflater)
        progress = ProgressDialog(context,5)
        progress.setTitle("Svindo Business")
        progress.setMessage("Loading, Please wait.")
        progress.setCanceledOnTouchOutside(true)
        progress.setCancelable(false)


        binding = AddonlayoutdesignBinding.inflate(inflater, container, false)
        binding = AddonlayoutdesignBinding.inflate(layoutInflater)

        linearLayoutManager = LinearLayoutManager(context)
        AddonBinding.AddonsRecyclerview.layoutManager = linearLayoutManager
        AddonBinding.AddonsRecyclerview.hasFixedSize()


        AddonBinding.addonbtn.setOnClickListener {
            showAlertDialog()
            AddonIconsList()
        }
//
//
//        resultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val uriPathHelper = URIPathHelper()
//                    val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
//
//                    val bitmap = imageUri?.let { decodeUri(it) }
//                    bannersBinding.bannerImage.setImageBitmap(bitmap)
//                    filePath?.let { file_1 = compressImage(filePath, 0.5)}
//                }
//            }
//        galleryResultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data = result.data
//                    imageUri = data!!.data
//
//                    val uriPathHelper = URIPathHelper()
//                    try {
//                        val filePath = imageUri?.let { uriPathHelper.getPath(this, it) }
//
//                        val bitmap = imageUri?.let { decodeUri(it) }
//                        bannersBinding.bannerImage.setImageBitmap(bitmap)
//
//                        filePath?.let {
//                            file_1 = compressImage(filePath, 0.5)
//                        }
//                        showToast("Image Selected")
//                    } catch (e: Exception) {
//                        showToast("Image Not Selected")
//                    }
//                }
//            }
        if (checkForInternet(requireContext())) {
            // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity(), NetworkIssueActivity::class.java)
            getActivity()?.startActivity(intent)
        }


        AddAddonsListdetails()
        return AddonBinding.root
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

//    private fun gallery() {
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//        }else if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1)
//        }else {
//            openGallery()
//        }
//    }

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//        galleryResultLauncher.launch(intent)
//    }

//    private fun decodeUri(uri: Uri): Bitmap? {
//        val inputStream =  Context.getContentResolver().openInputStream(uri)
//        val options = BitmapFactory.Options()
//        options.inPreferredConfig = Bitmap.Config.RGB_565
//        return BitmapFactory.decodeStream(inputStream, null, options)
//    }



    internal fun showAlertDialog() {
        builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val rootView = binding.root
        // Check if the rootView already has a parent
        val parent = rootView.parent as? ViewGroup
        parent?.removeView(rootView) // Remo

        builder.setView(rootView)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        binding.submitbutton.setOnClickListener {
            val addon_name = binding.addonnameEt.text.toString().trim()
            val addon_desc = binding.addondescEt.text.toString().trim()
            val addon_price = binding.addonpriceet.text.toString().trim()
            if (addon_name.isNotEmpty() && addon_price.isNotEmpty()) {
                progress.show()
                AddAddonsdetails(
                    addon_name = binding.addonnameEt.text.toString().trim(),
                    addon_desc = binding.addondescEt.text.toString().trim(),
                    addon_price = binding.addonpriceet.text.toString().trim(),
                    addon_id=addon_id.toString().trim()
                )
            }
            else{
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
           // alertDialog.hide()

        }
    }

    fun AddAddonsdetails(
        addon_name:String,
        addon_desc:String,
        addon_price:String,
        addon_id:String
    ){
        AddonBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try{
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddAddonsdetails(sharedPreference.getValueString("token"),addon_name,addon_desc,addon_price,addon_id)
            requestCall.enqueue(object : Callback<Bankdetails_Response> {
                override fun onResponse(
                    call: Call<Bankdetails_Response>,
                    response: Response<Bankdetails_Response>
                ) {
                    AddonBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                addonsresponse = response.body()!!
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        if (response.body()!!.error == "0") {
                                            Toast.makeText(
                                                context,
                                                addonsresponse.message.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            AddAddonsListdetails()
                                            alertDialog.hide()
                                            progress.dismiss()
                                        } else {
                                            progress.dismiss()
                                            //   Toast.makeText(context,addonsresponse.message.toString(), Toast.LENGTH_SHORT).show()
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
                override fun onFailure(call: Call<Bankdetails_Response>, t: Throwable) {
                    AddonBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    progress.dismiss()
                    Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    fun AddAddonsListdetails()
    {
        AddonBinding.progressBarLay.progressBarLayout.visibility = View.VISIBLE
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddAddonsListDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AddonsListModal> {
                override fun onResponse(
                    call: Call<AddonsListModal>,
                    response: Response<AddonsListModal>
                ) {
                    AddonBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                    addonsListresponse = response.body()!!
                                    if (addonsListresponse!=null) {
                                    if(addonsListresponse.error=="0")
                                    {
                                        if (addonsListresponse.add_on_list.count()>0) {
                                        AddonBinding.AddonsRecyclerview.visibility = View.VISIBLE
                                        adapter = context?.let { AddonsListAdapter(addonsListresponse.add_on_list, context = it) }!!
                                        AddonBinding.AddonsRecyclerview.adapter = adapter
                                    }
                                    }else{
                                      //  Toast.makeText(context,(addonsListresponse.message).toString(),Toast.LENGTH_SHORT).show()
                                    }

                                }

                            }

                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp),Toast.LENGTH_SHORT).show()

                            }else -> {
                            Toast.makeText(context,getString(R.string.server_error),Toast.LENGTH_SHORT).show()
                        }
                        }


                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddonsListModal>, t: Throwable) {
                    AddonBinding.progressBarLay.progressBarLayout.visibility = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    fun AddonIconsList() {
        try {
            val ordersService = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = ordersService.AddonsIconsDetails(sharedPreference.getValueString("token"))
            requestCall.enqueue(object : Callback<AddonIconsModal> {
                override fun onResponse(
                    call: Call<AddonIconsModal>,
                    response: Response<AddonIconsModal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                addonsIconsModalresponse = response.body()!!
                                if (addonsIconsModalresponse.error == "0") {
                                        AddonIconsSpinner(response.body()!!.addons)
                                    } else {

                                    }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp),Toast.LENGTH_SHORT).show()

                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error),Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out),Toast.LENGTH_SHORT).show()
                    }

                override fun onFailure(call: Call<AddonIconsModal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    internal fun AddonIconsSpinner(items: List<AddonXX>) {
        spinner = binding.Addonspinnerview
        val adapter = AddonIconsAdapter(requireContext(), items)
        spinner.adapter = adapter
        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                addon_id = selectedItem.id
                fetch_addon_details(addon_id)
                Picasso.get().load(selectedItem.image).into(binding.Addonimage)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected
            }
        }
    }

    fun fetch_addon_details(id:String){
        try {
            val add_addon_Service = ApiClient.buildService(ApiInterface::class.java)
            val requestCall = add_addon_Service.Addonsdetails(sharedPreference.getValueString("token"),id)
            requestCall.enqueue(object : Callback<Addon_details_modal> {
                override fun onResponse(
                    call: Call<Addon_details_modal>,
                    response: Response<Addon_details_modal>
                ) = //dashboardBinding.progressBarLay.visibility  = View.GONE
                    try {
                        when {
                            response.code() == 200 -> {
                                addon_details_response = response.body()!!
                                if (addon_details_response.error == "0") {
                                    binding.addonnameEt.setText(addon_details_response.addon_details.name)
                                    binding.addondescEt.setText(addon_details_response.addon_details.description)
                                } else {

                                }
                            }
                            response.code() == 401 -> {
                                Toast.makeText(context,getString(R.string.session_exp),Toast.LENGTH_SHORT).show()

                            }
                            else -> {
                                Toast.makeText(context,getString(R.string.server_error),Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: TimeoutException) {
                        Toast.makeText(context,getString(R.string.time_out),Toast.LENGTH_SHORT).show()
                    }

                override fun onFailure(call: Call<Addon_details_modal>, t: Throwable) {
                    //  dashboardBinding.progressBarLay.visibility  = View.GONE
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            //dashboardBinding.progressBarLay.visibility = View.GONE
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }
}





