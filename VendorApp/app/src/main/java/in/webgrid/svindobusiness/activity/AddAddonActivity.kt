package `in`.webgrid.svindobusiness.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.webgrid.svindobusiness.R
import `in`.webgrid.svindobusiness.Utils.SharedPreference
import `in`.webgrid.svindobusiness.databinding.ActivityAddAddonBinding
import `in`.webgrid.svindobusiness.modelclass.NotificationModal


@SuppressLint("StaticFieldLeak", "Registered")

class AddAddonActivity : AppCompatActivity() {
    private lateinit var addadonbinding: ActivityAddAddonBinding
    private lateinit var sharedPreference: SharedPreference
    lateinit var addonResponse: NotificationModal
//    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
//    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
//    private var imageUri: Uri? = null
//    private  var file_1: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_addon)
    addadonbinding = ActivityAddAddonBinding.inflate(layoutInflater)
    //sharedPreference = SharedPreference(this)
    setContentView(addadonbinding.root)
}
}