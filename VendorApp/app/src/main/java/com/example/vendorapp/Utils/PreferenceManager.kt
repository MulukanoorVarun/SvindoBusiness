package com.example.vendorapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.GeneralSecurityException

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