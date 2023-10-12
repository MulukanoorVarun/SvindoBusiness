package`in`.webgrid.svindobusiness.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context, name: String?) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun editor(): SharedPreferences.Editor? {
        return sharedPreferences!!.edit()
    }

    fun preference(): SharedPreferences? {
        return sharedPreferences
    }

    fun remove(key: String?) {
        sharedPreferences!!.edit().remove(key).apply()
    }

    operator fun contains(key: String?): Boolean {
        return sharedPreferences!!.contains(key)
    }

    fun clear() {
        sharedPreferences!!.edit().clear().apply()
    }
}