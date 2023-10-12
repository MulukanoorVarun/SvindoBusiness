package `in`.webgrid.svindobusiness.Utils
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
//import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import `in`.webgrid.svindobusiness.R


import java.io.File

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check inter net over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
//    } else {
//        return connectivityManager.activeNetworkInfo?.isConnected ?: false
//    }
    return false
}

fun Activity.closeKeyBoard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


fun Activity.stopNetworkCallback() {
    val cm: ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
}
/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}
/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
internal object SharedPreferenceUtil {

    const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
            .getBoolean(KEY_FOREGROUND_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        ).edit {
            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
        }
}
fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}


// logs



// fun compressImage(filePath: String, targetMB: Double = 1.0) : File {
//        var file = File(filePath)
//        var fullSizeBitmap: Bitmap = BitmapFactory.decodeFile(filePath)
//        val exif = ExifInterface(filePath)
//        val exifOrientation: Int = exif.getAttributeInt(
//            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
//        )
//        val exifDegree: Int = exifOrientationToDegrees(exifOrientation)
//        fullSizeBitmap = rotateImage(fullSizeBitmap, exifDegree.toFloat())
//
//        try {
//
//            val fileSizeInMB = getFileSizeInMB(filePath)
//
//            var quality = 100
//            if(fileSizeInMB > targetMB){//1.0 means target MB
//                quality = ((targetMB/fileSizeInMB)*100).toInt()
//            }
//            val fileOutputStream = FileOutputStream(filePath)
//            fullSizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
//            fileOutputStream.close()
//            file = File(filePath)
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
//    return file
//}

 fun getFileSizeInMB(filePath: String): Double{
    val file = File(filePath)
    val length = file.length()
    val fileSizeInKB = (length/1024).toString().toDouble()
    return  (fileSizeInKB/1024).toString().toDouble()
}

// fun exifOrientationToDegrees(exifOrientation: Int): Int {
//    return when(exifOrientation){
//        ExifInterface.ORIENTATION_ROTATE_90 -> 90
//        ExifInterface.ORIENTATION_ROTATE_180 -> 180
//        ExifInterface.ORIENTATION_ROTATE_270 -> 270
//        else -> 0
//    }
//}
 fun rotateImage(fullSizeBitmap: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(fullSizeBitmap, 0, 0, fullSizeBitmap.width, fullSizeBitmap.height, matrix, true)


}